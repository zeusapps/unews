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
    }
}
