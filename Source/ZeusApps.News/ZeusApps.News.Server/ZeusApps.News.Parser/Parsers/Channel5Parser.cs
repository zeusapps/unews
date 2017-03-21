using System;
using System.Threading.Tasks;
using System.Xml;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Core.Models;
using ZeusApps.News.Parser.Extensions;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser.Parsers
{
    public class Channel5Parser : ParserBase
    {
        public Channel5Parser(
            ICommunicationService repository,
            ILoggerFactory loggerFactory) : 
            base(repository, loggerFactory)
        {
        }

        protected override string ParseHtml(
            SourceDownloadableDto source,
            ArticleDownloadableDto article)
        {
            return GetDocumentNode(article.OriginalHtml, "//div[@class='article-body']", true)
                .UpdateSource(source.BaseUrl)?.InnerHtml;
        }

        protected override string ParseGuid(ArticleDownloadableDto article)
        {
            var link = article.Url;

            var dashPosition = link.LastIndexOf("-", StringComparison.CurrentCultureIgnoreCase);
            if (dashPosition == -1)
            {
                return link;
            }

            var pointPosition = link.LastIndexOf(".", StringComparison.CurrentCultureIgnoreCase);
            if (pointPosition == -1 || pointPosition < dashPosition)
            {
                return link;
            }

            dashPosition++;
            var guid = link.Substring(dashPosition, pointPosition - dashPosition);

            return string.IsNullOrEmpty(guid)
                ? link
                : guid;
        }

        protected override void ToArticleOverride(RssItem rssItem, SourceDownloadableDto source, ArticleDownloadableDto article)
        {
            if (!string.IsNullOrEmpty(article.FullText))
            {
                return;
            }

            try
            {
                var node = rssItem.Node;
                var manager = new XmlNamespaceManager(node.OwnerDocument.NameTable);
                manager.AddNamespace("yandex", "http://news.yandex.ru");

                article.FullText = rssItem.Node.SelectSingleNode("//yandex:full-text", manager)?.InnerText;
            }
            catch
            {
                //
            }
        }

        protected override Task<string> DownloadHtml(ArticleDownloadableDto article, SourceDownloadableDto source)
        {
            return string.IsNullOrEmpty(article.FullText)
                ? base.DownloadHtml(article, source)
                : Task.FromResult(article.FullText);
        }
    }
}
