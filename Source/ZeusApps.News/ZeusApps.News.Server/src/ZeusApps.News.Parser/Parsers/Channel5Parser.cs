using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Infrastructure;

namespace ZeusApps.News.Parser.Parsers
{
    public class Channel5Parser : IParser
    {
        public Task<Article[]> Parse(Source source)
        {
            return null;
        }
    }
}
