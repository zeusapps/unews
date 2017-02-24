using System.Threading.Tasks;
using ZeusApps.News.Models;

namespace ZeusApps.News.Parser.Infrastructure
{
    public interface IParser
    {
        Task Parse(Source source);
    }
}
