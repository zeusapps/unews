using System;
using System.Linq.Expressions;
using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Bson.Serialization;
using MongoDB.Bson.Serialization.IdGenerators;
using MongoDB.Bson.Serialization.Serializers;
using MongoDB.Driver;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Options;
using ZeusApps.News.Server.Repositories.Abstraction;

namespace ZeusApps.News.Server.Repositories
{
    public class ArticleRepository : RepositoryBase<Article>, IArticleRepository
    {
        public ArticleRepository(
            IOptions<ConnectionStringsOptions> options) : base(options)
        {
            Collection
                .Indexes
                .CreateOne(Builders<Article>.IndexKeys.Ascending(x => x.Url));
        }

        static ArticleRepository()
        {
            BsonClassMap
                .RegisterClassMap<Article>(x =>
                {
                    x.AutoMap();
                    x.GetMemberMap(s => s.Id)
                        .SetIdGenerator(new StringObjectIdGenerator())
                        .SetSerializer(new StringSerializer(BsonType.ObjectId));
                });
        }

        public async Task<Article[]> GetArticles(string sourceId, int count, int offset, DateTime? dateTime, bool isAfter)
        {
            var aggr = Collection
                .Aggregate()
                .Match(x => x.SourceId == sourceId)
                .Match(x => x.IsClean);

            if (dateTime != null)
            {
                var filter = isAfter
                    ? Builders<Article>.Filter.Lt(x => x.Published, dateTime.Value)
                    : Builders<Article>.Filter.Gt(x => x.Published, dateTime.Value);

                aggr = aggr.Match(filter);
            }

            var articles = await aggr
                .Sort(Builders<Article>.Sort.Descending(x => x.Published))
                .Skip(offset)
                .Limit(count)
                .ToListAsync();

            return articles.ToArray();
        }

        public Task<Article> GetArticle(string id)
        {
            return Collection
                .Aggregate()
                .Match(x => x.Id == id)
                .FirstOrDefaultAsync();
        }

        public async Task<bool> AddArticle(Article article)
        {
            await Collection.InsertOneAsync(article);

            return true;
        }

        public async Task<bool> DeleteArticle(string id)
        {
            var result = await Collection.FindOneAndDeleteAsync(Builders<Article>.Filter.Eq(x => x.Id, id));

            return result != null;
        }

        public async Task<bool> ContainsGuid(string sourceId, string guid)
        {
            var article = await Collection
                .Aggregate()
                .Match(x => x.SourceId == sourceId && x.Guid == guid)
                .FirstOrDefaultAsync();

            return article != null;
        }

        public Task<bool> Upvote(string id)
        {
            return Vote(x => x.Upvote, id);
        }

        public Task<bool> Downvote(string id)
        {
            return Vote(x => x.Downvote, id);
        }

        private async Task<bool> Vote(Expression<Func<Article, int>> getter, string id)
        {
            var filter = Builders<Article>.Filter.Eq(x => x.Id, id);
            var update = Builders<Article>.Update.Inc(getter, 1);

            var result = await Collection.FindOneAndUpdateAsync(filter, update);
            return result != null;
        }
    }
}
