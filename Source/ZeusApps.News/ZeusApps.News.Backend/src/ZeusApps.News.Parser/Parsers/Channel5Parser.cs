using Microsoft.Extensions.Logging;
using ZeusApps.News.Parser.Extensions;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public class Channel5Parser : ParserBase
    {
        public Channel5Parser(
            IArticleRepository repository,
            ILoggerFactory loggerFactory) : 
            base(repository, loggerFactory)
        {
        }

        protected override string ParseHtml(string html)
        {
            return GetDocumentNode(html, "//div[@class='article-content']")
                .UpdateSource(Source.BaseUrl)?.InnerHtml;
        }
    }
}
