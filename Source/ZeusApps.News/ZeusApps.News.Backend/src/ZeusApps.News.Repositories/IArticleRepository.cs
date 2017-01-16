using System.Threading.Tasks;
using ZeusApps.News.Models;

namespace ZeusApps.News.Repositories
{
    public interface IArticleRepository
    {
        Task<Article[]> GetArticles(string sourceId, int count, int offset);

        Task<Article> GetArticle(string id);

        Task<bool> AddArticle(Article article);

        Task<bool> DeleteArticle(string id);

        Task<bool> ContainsUrl(string url);

        Task<bool> Upvote(string id);

        Task<bool> Downvote(string id);
    }
}
