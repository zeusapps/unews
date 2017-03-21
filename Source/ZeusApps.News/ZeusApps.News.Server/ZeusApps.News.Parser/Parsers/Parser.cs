using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Parser.Options;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser.Parsers
{
    public class Parser
    {
        private readonly Uri _downloadUri = new Uri("/api/sources/downloadable", UriKind.Relative);
        private readonly ICommunicationService _communicationService;
        private readonly IDependencyResolverService _dependencyResolverService;
        private readonly ExecuteOptions _options;
        private readonly ILogger _logger;

        public Parser(
            IOptions<ExecuteOptions> executeOptions,
            ICommunicationService communicationService,
            IDependencyResolverService dependencyResolverService,
            ILoggerFactory loggerFactory)
        {
            _communicationService = communicationService;
            _dependencyResolverService = dependencyResolverService;
            _options = executeOptions.Value;
            _logger = loggerFactory.CreateLogger<Parser>();
        }

        public async Task Parse()
        {
            while (true)
            {
                try
                {
                    var sources = await _communicationService
                        .Get<SourceDownloadableDto[]>(_downloadUri);

                    foreach (var source in sources)
                    {
                        var parser = _dependencyResolverService.ResolveService<IParser>(source.Key);
                        if (parser == null)
                        {
                            _logger.LogError($"Could not resolve parser for \'{source.Title}\'");
                        }
                        else
                        {
                            await parser.Parse(source);
                        }
                    }

                    await Task.Delay(_options.TaskDelay);
                }
                catch (Exception e)
                {
                    _logger.LogError(1, e, e.Message);
                }         
            }
        }
    }
}
