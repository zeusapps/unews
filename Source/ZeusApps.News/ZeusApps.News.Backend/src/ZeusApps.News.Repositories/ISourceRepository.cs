using System.Threading.Tasks;
using ZeusApps.News.Models;

namespace ZeusApps.News.Repositories
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
