using System;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using ZeusApps.News.Server.Options;

namespace ZeusApps.News.Server.Repositories
{
    public class RepositoryBase<T>
    {
        private readonly string _connectionString;
        private readonly string _databaseName;
        private readonly string _collectionName;
        private IMongoClient _client;


        protected IMongoClient Client => _client ?? (_client = new MongoClient(_connectionString));
        protected IMongoDatabase Database => Client?.GetDatabase(_databaseName);
        protected IMongoCollection<T> Collection => GetCollection<T>(_collectionName);


        public RepositoryBase(IOptions<ConnectionStringsOptions> options) : this(options, null)
        {
            _collectionName = Pluralize(typeof(T).Name);
        }

        public RepositoryBase(IOptions<ConnectionStringsOptions> options, string collectionName)
        {
            if (options?.Value == null)
            {
                throw new NullReferenceException($"{nameof(ConnectionStringsOptions)} is undefined.");
            }

            _collectionName = collectionName;
            _connectionString = options.Value.Path;
            _databaseName = options.Value.Database;
        }

        protected IMongoCollection<TItem> GetCollection<TItem>(string collectionName) =>
            Database?.GetCollection<TItem>(collectionName);

        private static string Pluralize(string text)
        {
            var lower = text.ToLower();

            if (lower.EndsWith("y"))
            {
                return text.Substring(0, text.Length - 1) + "ies";
            }

            return text + "s";

        }
    }
}
