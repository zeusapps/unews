using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Infrastructure;
using Microsoft.AspNetCore.Mvc.Routing;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.Extensions;
using ZeusApps.News.Server.Options;
using ZeusApps.News.Server.Repositories;
using ZeusApps.News.Server.Repositories.Abstraction;
using ZeusApps.News.Server.Services;
using ZeusApps.News.Server.Services.Abstraction;

namespace ZeusApps.News.Server
{
    public class Startup
    {
        public IConfigurationRoot Configuration { get; }

        public Startup(IHostingEnvironment env)
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(env.ContentRootPath)
                .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                .AddJsonFile($"appsettings.{env.EnvironmentName}.json", optional: true)
                .AddEnvironmentVariables();
            Configuration = builder.Build();
        }

        public void ConfigureServices(IServiceCollection services)
        {
            // Add framework services.
            services.AddMvc();
            services
                .AddLogging()
                .AddSingleton<IArticleRepository, ArticleRepository>()
                .AddSingleton<ISourceRepository, SourceRepository>()
                .AddSingleton<IMapperService, MapperService>()
                .AddSingleton<IActionContextAccessor, ActionContextAccessor>()
                .AddTransient<IUrlHelper, UrlHelper>(x => new UrlHelper(x.GetService<IActionContextAccessor>().ActionContext))
                .AddOptions();

            services.ConfigureOptions<ConnectionStringsOptions>(Configuration);

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(
            IApplicationBuilder app,
            IHostingEnvironment env,
            ILoggerFactory loggerFactory,
            ISourceRepository sourceRepository)
        {
            loggerFactory.AddConsole(Configuration.GetSection("Logging"));
            loggerFactory.AddDebug();

            app.UseMvc();
        }
    }
}
