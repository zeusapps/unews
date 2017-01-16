using System;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using ZeusApps.News.Repository.Mongo.Options;

namespace ZeusApps.News.Repository.Mongo.Base
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


        public RepositoryBase(IOptions<ConnectionStringsOption> options) : this(options, null)
        {
            _collectionName = Pluralize(typeof(T).Name);
        }

        public RepositoryBase(IOptions<ConnectionStringsOption> options, string collectionName)
        {
            if (options?.Value == null)
            {
                throw new NullReferenceException($"{nameof(ConnectionStringsOption)} is undefined.");
            }

            _collectionName = collectionName;
            _connectionString = options.Value.Default;
            _databaseName = string.IsNullOrWhiteSpace(_connectionString)
                ? ""
                : MongoUrl.Create(_connectionString).DatabaseName;
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
