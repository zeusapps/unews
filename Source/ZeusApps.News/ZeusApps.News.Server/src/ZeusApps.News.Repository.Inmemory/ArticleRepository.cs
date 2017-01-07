﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Repository.Inmemory
{
    public class ArticleRepository: IArticleRepository
    {
        private readonly List<Article> _articles = new List<Article>();


        public Task<Article[]> GetArticles(string sourceId, int count, int offset)
        {
            var articles = _articles
                .Where(x => x.SourceId == sourceId)
                .Skip(offset)
                .Take(count)
                .ToArray();

            return Task.FromResult(articles);
        }

        public Task<Article> GetArticle(string id)
        {
            var article = _articles
                .FirstOrDefault(x => x.Id == id);

            return Task.FromResult(article);
        }

        public Task<bool> AddArticle(Article article)
        {



            article.Id = Guid.NewGuid().ToString();
            _articles.Add(article);

            return Task.FromResult(true);
        }

        public Task<bool> DeleteArticle(string id)
        {
            var article = _articles.FirstOrDefault(x => x.Id == id);
            if (article == null)
            {
                return Task.FromResult(false);
            }

            _articles.Remove(article);
            return Task.FromResult(true);
        }

        public Task<bool> ContainsUrl(string url)
        {
            var result = _articles.FirstOrDefault(x => x.Url == url);
            
            return Task.FromResult(result != null);
        }
    }
}
