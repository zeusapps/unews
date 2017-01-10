using System;
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


            var korrSource = new Source
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
            var channel5Source = new Source
            {
                Key = "channel5",
                Title = "Channel 5",
                SourceUrl = "http://www.5.ua/novyny/rss/",
                BaseUrl = "http://www.5.ua/",
                Encoding = "utf-8",
                Id = Guid.NewGuid().ToString(),
                IsDownloadable = true,
                IsReadable = true
            };


            ParserFactory.Parse(channel5Source)?.Wait();
        }
    }
}
