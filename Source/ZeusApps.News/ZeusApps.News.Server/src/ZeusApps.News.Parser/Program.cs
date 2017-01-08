using System;
using Microsoft.Extensions.Configuration;
using ZeusApps.News.Models;
using ZeusApps.News.Parser.Parsers;
using ZeusApps.News.Repositories;
using ZeusApps.News.Repository.Inmemory;

namespace ZeusApps.News.Parser
{
    public class Program
    {
        public static void Main(string[] args)
        {
            //var builder = new ConfigurationBuilder()
            //    .SetBasePath(AppContext.BaseDirectory)
            //    .AddJsonFile("settings.json", optional: true, reloadOnChange: true);


            IArticleRepository articleRepository = new ArticleRepository();
            var source = new Source
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

            var parser = new KorrParser(articleRepository);

            var task = parser.Parse(source);
            task.Wait();
        }
    }
}
