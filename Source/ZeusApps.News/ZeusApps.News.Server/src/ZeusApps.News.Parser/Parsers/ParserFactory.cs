using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Inmemory;

namespace ZeusApps.News.Parser.Parsers
{
    public static class ParserFactory
    {
        public static Task Parse(Source source)
        {
            IParser parser = null;
            IArticleRepository articleRepository = new ArticleRepository();

            switch (source.Key)
            {
                case "channel5":
                    parser = new Channel5Parser(articleRepository);
                    break;
                case "korr":
                    parser = new KorrParser(articleRepository);
                    break;
            }

            return parser?.Parse(source);
        }
    }
}
