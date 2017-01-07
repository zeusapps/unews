using System;
using Microsoft.Extensions.Configuration;

namespace ZeusApps.News.Parser
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(AppContext.BaseDirectory)
                .AddJsonFile("settings.json", optional: true, reloadOnChange: true);
        }
    }
}
