using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml;
using HtmlAgilityPack;
using Microsoft.Extensions.Logging;
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
        protected readonly ILogger Logger;

        protected ParserBase(
            IArticleRepository repository,
            ILoggerFactory loggerFactory)
        {
            Repository = repository;
            Logger = loggerFactory.CreateLogger(GetType());
        }

        public virtual async Task Parse(Source source)
        {
            Source = source;

            Logger.LogInformation($"Starting parse {source.Title}");

            var rss = await GetSource(source.SourceUrl);
            var items = GetRssItems(rss);
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
                await Repository.AddArticle(article);
            }
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
                    Logger.LogError("Failed to retreve source data", e);
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
                Logger.LogError("Failed to load rss items", e);
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
                Logger.LogError("Failed to parse rss items", e);
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
            string html;

            try
            {
                using (var http = new HttpClient())
                {
                    Logger.LogInformation($"Downloading article from {article.Url}");
                    html = await http.GetStringAsync(article.Url);
                }
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to download html", e);
                return false;
            }

            try
            {
                article.OriginalHtml = html;
                article.Html = ParseHtml(html);
                return true;
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to download html", e);
                return false;
            }
        }

        protected Task<bool> Contains(string url)
        {
            return Repository.ContainsUrl(url);
        }

        protected HtmlNode GetDocumentNode(string html, string selector = null)
        {
            HtmlNode node;
            try
            {
                var doc = new HtmlDocument();
                doc.LoadHtml(html);
                node = doc.DocumentNode;
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to load html document", e);
                return null;
            }

            return string.IsNullOrEmpty(selector) 
                ? node 
                : node.SelectSingleNode(selector);
        }
    }
}
