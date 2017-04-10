using System;
using System.Threading.Tasks;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Parameters;

namespace ZeusApps.News.Server.Repositories.Abstraction
{
    public interface IArticleRepository
    {
        Task<Article[]> GetArticles(ArticlesParameters parameters);

        Task<Article> GetArticle(string id);

        Task<bool> AddArticle(Article article);

        Task<bool> DeleteArticle(string id);

        Task<bool> ContainsGuid(string sourceId, string guid);

        Task<bool> Upvote(string id);

        Task<bool> Downvote(string id);
    }
}
