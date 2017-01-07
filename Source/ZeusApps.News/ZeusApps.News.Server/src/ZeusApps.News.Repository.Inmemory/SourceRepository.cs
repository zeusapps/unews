using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Repository.Inmemory
{
    public class SourceRepository : ISourceRepository
    {
        private readonly List<Source> _sources = new List<Source>();

        public Task<Source[]> GetDownloadableSources()
        {
            var sources = _sources
                .Where(x => x.IsDownloadable)
                .ToArray();

            return Task.FromResult(sources);
        }

        public Task<Source[]> GetReadableSources()
        {
            var sources = _sources
                .Where(x => x.IsReadable)
                .ToArray();

            return Task.FromResult(sources);
        }

        public Task<Source[]> GetSources()
        {
            var sources = _sources.ToArray();

            return Task.FromResult(sources);
        }

        public Task<Source> GetSource(string id)
        {
            var source = _sources.FirstOrDefault(x => x.Id == id);

            return Task.FromResult(source);
        }

        public Task<bool> AddSource(Source source)
        {
            source.Id = Guid.NewGuid().ToString();

            _sources.Add(source);
            return Task.FromResult(true);
        }
    }
}
