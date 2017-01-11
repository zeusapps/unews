using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Parser.Options;
using ZeusApps.News.Parser.Parsers;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Inmemory;

namespace ZeusApps.News.Parser
{
    public class Program
    {
        private static IServiceProvider _serviceProvider;
        private static IConfigurationRoot _configuration;

        public static void Main(string[] args)
        {
            _configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("settings.json", true, true)
                .Build();

            var collection = new ServiceCollection();
            AddServices(collection);
            _serviceProvider = collection.BuildServiceProvider();

            Run().Wait();
        }

        public static void AddServices(IServiceCollection serviceCollection)
        {
            serviceCollection.AddLogging()
                .AddSingleton<IArticleRepository, ArticleRepository>()
                .AddSingleton<ISourceRepository, SourceRepository>()
                .AddSingleton<IParser, ParserImpl>();

            serviceCollection.AddOptions();
            serviceCollection.Configure<ExecuteOptions>(_configuration.GetSection(ExecuteOptions.Key));
        }

        public static async Task Run()
        {
            var sourceRepository = _serviceProvider.GetService<ISourceRepository>();
            var parser = _serviceProvider.GetService<IParser>();

            while (true)
            {
                var sources = await sourceRepository.GetDownloadableSources();
                var options = _serviceProvider.GetService<IOptions<ExecuteOptions>>().Value;
                foreach (var source in sources)
                {
                    await parser.Parse(source);
                    await Task.Delay(TimeSpan.FromSeconds(options.ItemDelay));
                }
                
                await Task.Delay(TimeSpan.FromMinutes(options.TaskDelay));
            }
            // ReSharper disable once FunctionNeverReturns
        }
    }
}
