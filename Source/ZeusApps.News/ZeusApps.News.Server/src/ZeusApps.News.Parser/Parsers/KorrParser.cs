using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml;
using HtmlAgilityPack;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Models;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public class KorrParser
    {
        private readonly IArticleRepository _repository;

        public KorrParser(IArticleRepository repository)
        {
            _repository = repository;
        }
        
        public async Task<Article[]> Parse(Source source)
        {
            var rss = await GetSource(source.SourceUrl);
            var items = GetRssItems(rss);
            
            var result = new List<Article>();
            foreach (var item in items)
            {
                if (await _repository.ContainsUrl(item.Link))
                {
                    continue;
                }

                var article = ToArticle(item, source);
                if (await DownloadHtml(article, source))
                {
                    await _repository.AddArticle(article);
                }
            }

            return result.ToArray();
        }

        private static async Task<string> GetSource(string sourceUrl)
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

        private static IEnumerable<RssItem> GetRssItems(string rss)
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

        private static Article ToArticle(RssItem item, Source source)
        {
            return new Article
            {
                Downloaded = DateTime.UtcNow,
                ImageUrl = item.EnclosureImage?.Replace("/190x120/","/610x385/"),
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

        private static async Task<bool> DownloadHtml(Article article, Source source)
        {
            try
            {
                using (var http = new HttpClient())
                {
                    var html = await http.GetStringAsync(article.Url);

                    article.OriginalHtml = html;
                    article.Html = Parse(html);

                    return true;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return false;
            }
        }

        private static string Parse(string html)
        {
            var doc = new HtmlDocument();
            doc.LoadHtml(html);

            var node = doc.DocumentNode.SelectSingleNode("//div[@class='post-item clearfix']");
            Remove(node, "//h1[@class='post-item__title']");
            Remove(node, "//div[@class='post-item__info']");
            Remove(node, "//div[@class='post-item__header clearfix']");
            Remove(node, "//div[@class='post-item__photo clearfix']/div[@class='clearfix']");
            Remove(node, "//div[@class='mrt_small']");
            Remove(node, "//div[@class='post-item__source']");
            Remove(node, "//div[@class='post-item__tags clearfix']");

            RemoveStyles(node);

            return node.InnerHtml;
        }

        private static void Remove(HtmlNode node, string xpath)
        {
            var items = node.SelectNodes(xpath);
            if (items == null)
            {
                return;
            }

            foreach (var item in items)
            {
                item.Remove();
            }
        }

        private static void RemoveStyles(HtmlNode node)
        {
            var items = node.SelectNodes("//*[@style]");

            if (items == null)
            {
                return;
            }

            foreach (var item in items)
            {
                item.Attributes["style"].Remove();
            }
        }
    }
}
