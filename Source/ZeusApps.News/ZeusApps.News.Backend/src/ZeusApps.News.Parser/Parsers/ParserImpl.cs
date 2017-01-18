using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public class ParserImpl : IParser
    {
        private readonly IArticleRepository _repository;
        private readonly ILoggerFactory _loggerFactory;

        public ParserImpl(
            IArticleRepository repository,
            ILoggerFactory loggerFactory)
        {
            _repository = repository;
            _loggerFactory = loggerFactory;
        }

        public Task Parse(Source source)
        {
            IParser parser;

            switch (source.Key)
            {
                case "channel5":
                    parser = new Channel5Parser(_repository, _loggerFactory);
                    break;
                case "korr":
                    parser = new KorrParser(_repository, _loggerFactory);
                    break;
                default:
                    throw new NotImplementedException($"Not implemented processor for {source.Key}");
            }

            return parser.Parse(source);
        }
    }
}
