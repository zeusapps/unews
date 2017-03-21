using System.Threading.Tasks;
using ZeusApps.News.Core.DTOs;

namespace ZeusApps.News.Parser.Parsers
{
    public interface IParser
    {
        Task Parse(SourceDownloadableDto source);
    }
}