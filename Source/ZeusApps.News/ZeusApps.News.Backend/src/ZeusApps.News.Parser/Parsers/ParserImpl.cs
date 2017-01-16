using System;
using System.Threading.Tasks;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Repositories;

namespace ZeusApps.News.Parser.Parsers
{
    public class ParserImpl : IParser
    {
        private readonly IArticleRepository _repository;

        public ParserImpl(IArticleRepository repository)
        {
            _repository = repository;
        }

        public Task<Article[]> Parse(Source source)
        {
            IParser parser;

            switch (source.Key)
            {
                case "channel5":
                    parser = new Channel5Parser(_repository);
                    break;
                case "korr":
                    parser = new KorrParser(_repository);
                    break;
                default:
                    throw new NotImplementedException($"Not implemented processor for {source.Key}");
            }

            return parser.Parse(source);
        }
    }
}
