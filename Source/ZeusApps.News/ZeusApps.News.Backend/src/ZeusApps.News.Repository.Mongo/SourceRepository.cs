using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Bson.Serialization;
using MongoDB.Bson.Serialization.IdGenerators;
using MongoDB.Bson.Serialization.Serializers;
using MongoDB.Driver;
using ZeusApps.News.Models;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Mongo.Base;
using ZeusApps.News.Repository.Mongo.Options;

namespace ZeusApps.News.Repository.Mongo
{
    public class SourceRepository : RepositoryBase<Source>, ISourceRepository
    {
        public SourceRepository(IOptions<ConnectionStringsOption> options) : base(options)
        {
            BsonClassMap
                .RegisterClassMap<Source>(x =>
                {
                    x.AutoMap();
                    x.GetMemberMap(s => s.Id)
                        .SetIdGenerator(new StringObjectIdGenerator())
                        .SetSerializer(new StringSerializer(BsonType.ObjectId));
                });
        }

        public async Task<Source[]> GetDownloadableSources()
        {
            var sources = await Collection
                .Aggregate()
                .Match(x => x.IsDownloadable)
                .ToListAsync();

            return sources.ToArray();
        }

        public async Task<Source[]> GetReadableSources()
        {
            var sources = await Collection
                .Aggregate()
                .Match(x => x.IsReadable)
                .ToListAsync();

            return sources.ToArray();
        }

        public async Task<Source[]> GetSources()
        {
            var sources = await Collection
                .Aggregate()
                .Match(x=>true)
                .ToListAsync();

            return sources.ToArray();
        }

        public Task<Source> GetSource(string id)
        {
            return Collection
                .Aggregate()
                .Match(x => x.Id == id)
                .FirstOrDefaultAsync();
        }

        public async Task<bool> AddSource(Source source)
        {
            await Collection.InsertOneAsync(source);
            return true;
        }

        public void EnsureData()
        {
            var count = Collection.Count(s => true);
            if (count > 0)
            {
                return;
            }


            var korrSource = new Source
            {
                Key = "korr",
                ImageUrl = "http://news.img.com.ua/img/smalllogo.gif",
                Title = "Korrespondent.Net",
                SourceUrl = "http://k.img.com.ua/rss/ru/all_news2.0.xml",
                BaseUrl = "http://korrespondent.net/",
                Encoding = "utf-8",
                IsDownloadable = true,
                IsReadable = true
            };
            var channel5Source = new Source
            {
                Key = "channel5",
                Title = "Channel 5",
                SourceUrl = "http://www.5.ua/novyny/rss/",
                BaseUrl = "http://www.5.ua/",
                Encoding = "utf-8",
                IsDownloadable = true,
                IsReadable = true
            };

            Collection.InsertMany(new [] { korrSource, channel5Source });
        }
    }
}
