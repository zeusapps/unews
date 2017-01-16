using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml;
using HtmlAgilityPack;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Parser.Models;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public abstract class ParserBase : IParser
    {
        protected Source Source { get; private set; }

        protected readonly IArticleRepository Repository;

        protected ParserBase(IArticleRepository repository)
        {
            Repository = repository;
        }

        public virtual async Task<Article[]> Parse(Source source)
        {
            Source = source;

            var rss = await GetSource(source.SourceUrl);
            var items = GetRssItems(rss);

            var result = new List<Article>();
            foreach (var item in items)
            {
                if (await Contains(item.Link))
                {
                    continue;
                }

                var article = ToArticle(item, source);

                if (!await DownloadHtml(article, source))
                {
                    continue;
                }

                ArticleOverride(article);
                result.Add(article);
            }

            return result.ToArray();
        }

        protected virtual void ArticleOverride(Article article)
        {
            
        }

        protected abstract string ParseHtml(string html);

        protected async Task<string> GetSource(string sourceUrl)
        {
            using (var http = new HttpClient())
            {
                try
                {
                    return await http.GetStringAsync(sourceUrl);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                    return null;
                }

            }
        }

        protected IEnumerable<RssItem> GetRssItems(string rss)
        {
            XmlDocument doc;

            try
            {
                doc = new XmlDocument();
                doc.LoadXml(rss);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return new RssItem[0];
            }

            try
            {
                var items = doc.ChildNodes[1]
                    .ChildNodes[0]
                    .ChildNodes
                    .Cast<XmlElement>()
                    .Where(x => x.LocalName == "item")
                    .Select(x => new RssItem(x))
                    .ToArray();
                return items;
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return new RssItem[0];
            }
        }

        protected Article ToArticle(RssItem item, Source source)
        {
            return new Article
            {
                Downloaded = DateTime.UtcNow,
                ImageUrl = item.EnclosureImage,
                Published = item.PubDate.GetValueOrDefault(DateTime.UtcNow),
                SourceId = source.Key,
                Title = item.Title,
                Url = item.Link,
                Description = item.Description,
                FullText = item.FullText,
                Category = item.Category,
                Guid = item.Guid,
                Author = item.Author,
                Image = item.Image
            };
        }

        protected async Task<bool> DownloadHtml(Article article, Source source)
        {
            try
            {
                using (var http = new HttpClient())
                {
                    var html = await http.GetStringAsync(article.Url);

                    article.OriginalHtml = html;
                    article.Html = ParseHtml(html);

                    return true;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return false;
            }
        }

        protected Task<bool> Contains(string url)
        {
            return Repository.ContainsUrl(url);
        }

        protected HtmlNode GetDocumentNode(string html, string selector = null)
        {
            var doc = new HtmlDocument();
            doc.LoadHtml(html);

            var node = doc.DocumentNode;

            return string.IsNullOrEmpty(selector) 
                ? node 
                : node.SelectSingleNode(selector);
        }
    }
}
