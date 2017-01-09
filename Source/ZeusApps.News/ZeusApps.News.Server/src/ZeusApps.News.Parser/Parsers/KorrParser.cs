using System.Collections.Generic;
using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Extensions;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public class KorrParser : ParserBase
    {
        public KorrParser(IArticleRepository repository) : base(repository)
        {
        }
        
        public override async Task<Article[]> Parse(Source source)
        {
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
                article.Image = article.Image?.Replace("/190x120/", "/610x385/");

                if (await DownloadHtml(article, source))
                {
                    result.Add(article);
                }
            }

            return result.ToArray();
        }
        

        protected override string ParseHtml(string html)
        {
            return GetDocumentNode(html, "//div[@class='post-item clearfix']")
                .Remove("//h1[@class='post-item__title']")
                .Remove("//div[@class='post-item__info']")
                .Remove("//div[@class='post-item__header clearfix']")
                .Remove("//div[@class='post-item__photo clearfix']/div[@class='clearfix']")
                .Remove("//div[@class='mrt_small']")
                .Remove("//div[@class='post-item__source']")
                .Remove("//div[@class='post-item__tags clearfix']")
                .RemoveStyles()
                .InnerHtml;
        }

       
    }
}
