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

        public SourceRepository()
        {
            var korrSource = new Source
            {
                Key = "korr",
                ImageUrl = "http://news.img.com.ua/img/smalllogo.gif",
                Title = "Korrespondent.Net",
                SourceUrl = "http://k.img.com.ua/rss/ru/all_news2.0.xml",
                BaseUrl = "http://korrespondent.net/",
                Encoding = "utf-8",
                Id = Guid.NewGuid().ToString(),
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
                Id = Guid.NewGuid().ToString(),
                IsDownloadable = true,
                IsReadable = true
            };

            _sources.Add(korrSource);
            _sources.Add(channel5Source);
        }

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
