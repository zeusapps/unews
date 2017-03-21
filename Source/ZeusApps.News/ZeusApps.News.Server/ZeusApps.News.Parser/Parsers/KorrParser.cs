using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Parser.Extensions;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser.Parsers
{
    public class KorrParser: ParserBase
    {
        public KorrParser(
            ICommunicationService communicationService,
            ILoggerFactory loggerFactory) 
            : base(communicationService, loggerFactory)
        {
        }

        protected override void ArticleOverride(ArticleDownloadableDto article)
        {
            article.ImageUrl = article.ImageUrl?.Replace("/190x120/", "/610x385/");
        }

        protected override Task<string> DownloadHtml(ArticleDownloadableDto article, SourceDownloadableDto source)
        {
            return string.IsNullOrEmpty(article.FullText) 
                ? base.DownloadHtml(article, source) 
                : Task.FromResult(article.FullText);
        }

        protected override string ParseHtml(
            SourceDownloadableDto source,
            ArticleDownloadableDto article)
        {
            return GetDocumentNode(article.OriginalHtml, "//div[@class='post-item clearfix']", true)
                .Remove("//h1[@class='post-item__title']")
                .Remove("//div[@class='post-item__info']")
                .Remove("//div[@class='post-item__header clearfix']")
                .Remove("//div[@class='post-item__photo clearfix']/div[@class='clearfix']")
                .Remove("//div[@class='mrt_small']")
                .Remove("//div[@class='post-item__source']")
                .Remove("//div[@class='post-item__tags clearfix']")
                .RemoveStyles()?.InnerHtml;
        }
    }
}             