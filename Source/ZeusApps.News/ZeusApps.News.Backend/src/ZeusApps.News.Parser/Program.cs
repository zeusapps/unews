using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using ZeusApps.News.Parser.Infrastructure;
using ZeusApps.News.Parser.Options;
using ZeusApps.News.Parser.Parsers;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Mongo;
//using ZeusApps.News.Repository.Inmemory;
using ZeusApps.News.Repository.Mongo.Options;

namespace ZeusApps.News.Parser
{
    public class Program
    {
        private static IServiceProvider _serviceProvider;
        private static IConfigurationRoot _configuration;
        private static string _environment;

        public static void Main(string[] args)
        {
            _environment = args?.Length > 0 ? args[0] : "Development";


            _configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", true, true)
                .AddJsonFile($"appsettings.{_environment}.json", true, true)
                .Build();

            var collection = new ServiceCollection();
            AddServices(collection);
            _serviceProvider = collection.BuildServiceProvider();

            Run(
                _serviceProvider.GetService<ILoggerFactory>(),
                _serviceProvider.GetService<IParser>())
                .Wait();
        }

        public static void AddServices(IServiceCollection serviceCollection)
        {
            serviceCollection.AddLogging()
                .AddSingleton<IArticleRepository, ArticleRepository>()
                .AddSingleton<ISourceRepository, SourceRepository>()
                .AddSingleton<IParser, ParserImpl>();

            serviceCollection.AddOptions();
            serviceCollection.Configure<ExecuteOptions>(_configuration.GetSection(ExecuteOptions.Key));
            serviceCollection.Configure<ConnectionStringsOption>(_configuration.GetSection(ConnectionStringsOption.Key));
        }

        public static async Task Run(ILoggerFactory loggerFactory, IParser parser)
        {
            loggerFactory.AddConsole(_configuration.GetSection("Logging"));


            var sourceRepository = _serviceProvider.GetService<ISourceRepository>();
            var logger = loggerFactory.CreateLogger<Program>();
            while (true)
            {
                Console.Clear();
                var sources = await sourceRepository.GetDownloadableSources();
                var options = _serviceProvider.GetService<IOptions<ExecuteOptions>>().Value;

                logger.LogInformation($"Sources: {sources.Length}");

                foreach (var source in sources)
                {
                    logger.LogInformation($"{DateTime.Now} :: Starting to parse {source.Title}");
                    await parser.Parse(source);
                    logger.LogInformation($"{DateTime.Now} :: Parse of {source.Title} finished");
                    await Task.Delay(TimeSpan.FromSeconds(options.ItemDelay));
                }
                
                await Task.Delay(TimeSpan.FromMinutes(options.TaskDelay));
            }
            // ReSharper disable once FunctionNeverReturns
        }
    }
}
