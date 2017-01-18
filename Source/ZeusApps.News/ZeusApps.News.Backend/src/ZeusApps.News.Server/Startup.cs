using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Mongo;
using ZeusApps.News.Repository.Mongo.Options;
using ZeusApps.News.Server.Services;

namespace ZeusApps.News.Server
{
    public class Startup
    {
        public Startup(IHostingEnvironment env)
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(env.ContentRootPath)
                .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
                .AddJsonFile($"appsettings.{env.EnvironmentName}.json", optional: true)
                .AddEnvironmentVariables();
            Configuration = builder.Build();
        }

        public IConfigurationRoot Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            // Add framework services.
            services.AddMvc();
            services
                .AddLogging()
                .AddSingleton<IArticleRepository, ArticleRepository>()
                .AddSingleton<ISourceRepository, SourceRepository>()
                .AddSingleton<IMapperService, MapperService>()
                .AddOptions();

            services
                .Configure<ConnectionStringsOption>(
                    Configuration.GetSection(ConnectionStringsOption.Key));

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
        {
            loggerFactory.AddConsole(Configuration.GetSection("Logging"));
            loggerFactory.AddDebug();

            app.UseMvc();
        }
    }
}
