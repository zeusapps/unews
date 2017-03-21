using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using HtmlAgilityPack;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Core.Models;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser.Parsers
{
    public abstract class ParserBase : IParser
    {
        private readonly ICommunicationService _communicationService;
        //protected SourceDownloadableDto Source { get; private set; }
        protected readonly ILogger Logger;

        protected ParserBase(
            ICommunicationService communicationService,
            ILoggerFactory loggerFactory)
        {
            _communicationService = communicationService;
            Logger = loggerFactory.CreateLogger(GetType());
        }

        public virtual async Task Parse(SourceDownloadableDto source)
        {
            Logger.LogInformation($"Starting parse {source.Title}");

            var rss = await GetSource(source);
            var items = GetRssItems(rss);
            foreach (var item in items)
            {
                var article = ToArticle(item, source);
                article.Guid = ParseGuid(article);

                if (await Contains(source, article.Guid))
                {
                    continue;
                }

                var html = await DownloadHtml(article, source);
                if (string.IsNullOrEmpty(html))
                {
                    continue;
                }

                article.OriginalHtml = html;

                try
                {
                    article.Html = ParseHtml(source, article);
                }
                catch (Exception e)
                {
                    Logger.LogError("Failed to parse html", e);
                    continue;
                }

                ArticleOverride(article);
                await Save(article);
            }
        }

        protected virtual string ParseGuid(ArticleDownloadableDto article)
        {
            return article.Guid ?? article.Url;
        }

        protected virtual void ArticleOverride(ArticleDownloadableDto article)
        {
            
        }

        protected virtual void ToArticleOverride(RssItem rssItem, SourceDownloadableDto source, ArticleDownloadableDto article)
        {
            
        }

        protected abstract string ParseHtml(SourceDownloadableDto source, ArticleDownloadableDto html);

        protected async Task<string> GetSource(SourceDownloadableDto source)
        {
            var encoding = Encoding.GetEncoding(source.Encoding);
            var data = await _communicationService.GetString(new Uri(source.SourceUrl), encoding);
            return data;
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
                return doc
                    .SelectNodes("//item")
                    .OfType<XmlNode>()
                    .Select(x => new RssItem(x))
                    .ToArray();
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to parse rss items", e);
                return new RssItem[0];
            }
        }

        protected ArticleDownloadableDto ToArticle(RssItem item, SourceDownloadableDto source)
        {
            var article = new ArticleDownloadableDto
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

            ToArticleOverride(item, source, article);

            return article;
        }

        protected virtual async Task<string> DownloadHtml(ArticleDownloadableDto article, SourceDownloadableDto source)
        {
            try
            {
                Logger.LogInformation($"Downloading article from {article.Url}");
                var encoding = Encoding.GetEncoding(source.Encoding);
                return await _communicationService.GetString(new Uri(article.Url), encoding);
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to download html", e);
                return null;
            }
        }

        protected async Task<bool> Contains(SourceDownloadableDto source, string guid)
        {
            var uri = new Uri($"/api/sources/{source.Key}/articles/contains", UriKind.Relative);
            var article = new ArticleContainsDto{ Guid = guid };
            var result = await _communicationService.Post<bool>(uri, article);

            return result;
        }

        protected async Task Save(ArticleDownloadableDto article)
        {
            var uri = new Uri($"/api/sources/{article.SourceId}/articles/", UriKind.Relative);
            await _communicationService.Post<object>(uri, article);
        }

        protected HtmlNode GetDocumentNode(string html, string selector = null, bool allowFallback = false)
        {
            HtmlNode root;
            try
            {
                var doc = new HtmlDocument();
                doc.LoadHtml(html);
                root = doc.DocumentNode;
            }
            catch (Exception e)
            {
                Logger.LogError("Failed to load html document", e);
                return null;
            }

            if (string.IsNullOrEmpty(selector))
            {
                return root;
            }

            var node = root.SelectSingleNode(selector);
            if (node != null)
            {
                return node;
            }

            return allowFallback
                ? root
                : null;
        }
    }
}
