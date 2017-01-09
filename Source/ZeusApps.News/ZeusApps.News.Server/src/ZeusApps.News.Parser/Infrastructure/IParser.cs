using System.Threading.Tasks;
using ZeusApps.News.Models;

namespace ZeusApps.News.Parser.Infrastructure
{
    public interface IParser
    {
        Task<Article[]> Parse(Source source);
    }
}
