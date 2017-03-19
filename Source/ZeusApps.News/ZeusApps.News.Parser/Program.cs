using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Parser.Options;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser
{
    class Program
    {
        private static IServiceProvider _serviceProvider;
        private static IConfigurationRoot _configuration;
        
        public static void Main(string[] args)
        {
            _configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", true, true)
                //.AddJsonFile($"appsettings.{_environment}.json", true, true)
                .AddEnvironmentVariables()
                .Build();

            var collection = new ServiceCollection();
            AddServices(collection);
            _serviceProvider = collection.BuildServiceProvider();

            Run(ResolveService<ILoggerFactory>()).Wait();
        }

        public static void AddServices(IServiceCollection serviceCollection)
        {
            serviceCollection
                .AddLogging()
                .AddSingleton<IDependencyResolverService, DependencyResolverService>();
//                .AddSingleton<IArticleRepository, ArticleRepository>()
//                .AddSingleton<ISourceRepository, SourceRepository>()
//                .AddSingleton<IParser, ParserImpl>();

            serviceCollection.AddOptions();
            serviceCollection.Configure<ExecuteOptions>(_configuration);
        }

        public static async Task Run(ILoggerFactory loggerFactory)
        {
//            loggerFactory.AddConsole(_configuration.GetSection("Logging"));
//
//
//            var sourceRepository = _serviceProvider.GetService<ISourceRepository>();
//            var logger = loggerFactory.CreateLogger<Program>();
//            while (true)
//            {
//                Console.Clear();
//                var sources = await sourceRepository.GetDownloadableSources();
//                var options = _serviceProvider.GetService<IOptions<ExecuteOptions>>().Value;
//
//                logger.LogInformation($"Sources: {sources.Length}");
//
//                foreach (var source in sources)
//                {
//                    logger.LogInformation($"{DateTime.Now} :: Starting to parse {source.Title}");
//                    await parser.Parse(source);
//                    logger.LogInformation($"{DateTime.Now} :: Parse of {source.Title} finished");
//                    await Task.Delay(TimeSpan.FromSeconds(options.ItemDelay));
//                }
//
//                await Task.Delay(TimeSpan.FromMinutes(options.TaskDelay));
//            }
            // ReSharper disable once FunctionNeverReturns
        }

        public static T ResolveService<T>()
        {
            return _serviceProvider.GetService<T>();
        }

        public static IEnumerable<T> ResolveServices<T>()
        {
            return _serviceProvider.GetServices<T>();
        }
    }
}