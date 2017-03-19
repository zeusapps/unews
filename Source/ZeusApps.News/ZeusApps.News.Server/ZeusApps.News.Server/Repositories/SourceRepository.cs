using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Options;
using MongoDB.Bson;
using MongoDB.Bson.Serialization;
using MongoDB.Bson.Serialization.IdGenerators;
using MongoDB.Bson.Serialization.Serializers;
using MongoDB.Driver;
using Newtonsoft.Json;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Options;
using ZeusApps.News.Server.Repositories.Abstraction;

namespace ZeusApps.News.Server.Repositories
{
    public class SourceRepository : RepositoryBase<Source>, ISourceRepository
    {
        private readonly IHostingEnvironment _environment;

        public SourceRepository(
            IOptions<ConnectionStringsOptions> options,
            IHostingEnvironment environment) : base(options)
        {
            _environment = environment;
            EnsureData();
        }

        static SourceRepository()
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
                .Match(x => true)
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

            var path = Path.Combine(_environment.ContentRootPath, "Seed", "sources.json");

            var json = File.ReadAllText(path);
            if (string.IsNullOrEmpty(json))
            {
                return;
            }

            var sources = JsonConvert.DeserializeObject<Source[]>(json);
            Collection.InsertMany(sources);
        }
    }

}
