using System.Threading.Tasks;
using ZeusApps.News.Server.Models;

namespace ZeusApps.News.Server.Repositories.Abstraction
{
    public interface ISourceRepository
    {
        Task<Source[]> GetDownloadableSources();

        Task<Source[]> GetReadableSources();

        Task<Source[]> GetSources();

        Task<Source> GetSource(string id);

        Task<bool> AddSource(Source source);

        void EnsureData();
    }
}
