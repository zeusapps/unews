﻿using System;
using System.Linq.Expressions;
using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using ZeusApps.News.Models;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Mongo.Base;
using ZeusApps.News.Repository.Mongo.Options;

namespace ZeusApps.News.Repository.Mongo
{
    public class ArticleRepository : RepositoryBase<Article>, IArticleRepository
    {
        public ArticleRepository(IOptions<ConnectionStringsOption> options) : base(options)
        {
            Collection.Indexes.CreateOne(Builders<Article>.IndexKeys.Ascending(x => x.Url));
        }

        public async Task<Article[]> GetArticles(string sourceId, int count, int offset)
        {
            var articles = await Collection
                .Aggregate()
                .Match(x => x.SourceId == sourceId)
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

        public async Task<bool> ContainsUrl(string url)
        {
            var article = await Collection
                .Aggregate()
                .Match(x => x.Url == url)
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
