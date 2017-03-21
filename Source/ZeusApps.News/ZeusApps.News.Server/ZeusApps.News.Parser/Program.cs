using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.Extensions;
using ZeusApps.News.Parser.Options;
using ZeusApps.News.Parser.Parsers;
using ZeusApps.News.Parser.Services;

namespace ZeusApps.News.Parser
{
    class Program
    {
        private static IServiceProvider _serviceProvider;
        private static IConfigurationRoot _configuration;
        
        public static void Main(string[] args)
        {
            Encoding.RegisterProvider(CodePagesEncodingProvider.Instance);

            _configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", true, true)
                //.AddJsonFile($"appsettings.{_environment}.json", true, true)
                .AddEnvironmentVariables()
                .Build();

            var collection = new ServiceCollection();
            AddServices(collection);
            _serviceProvider = collection.BuildServiceProvider();

            Run(
                ResolveService<ILoggerFactory>(),
                ResolveService<Parsers.Parser>()
                ).Wait();
        }

        public static void AddServices(IServiceCollection serviceCollection)
        {
            serviceCollection
                .AddLogging()
                .AddSingleton<IDependencyResolverService, DependencyResolverService>()
                .AddSingleton<ICommunicationService, CommunicationService>()
                .AddSingleton<IParser, KorrParser>()
                .AddSingleton<IParser, Channel5Parser>()
                .AddSingleton<Parsers.Parser>()
                .AddOptions()
                .ConfigureOptions<ExecuteOptions>(_configuration);
        }

        public static Task Run(ILoggerFactory loggerFactory, Parsers.Parser parser)
        {
            loggerFactory.AddConsole(_configuration.GetSection("Logging"));
            return parser.Parse();
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