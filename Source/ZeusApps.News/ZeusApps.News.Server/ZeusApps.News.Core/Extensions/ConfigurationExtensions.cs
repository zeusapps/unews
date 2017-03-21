using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace ZeusApps.News.Core.Extensions
{
    public static class ConfigurationExtensions
    {
        private const string OPTIONS_TAIL = "Options";

        public static IServiceCollection ConfigureOptions<T>(
            this IServiceCollection collection,
            IConfigurationRoot configuration) 
            
            where T : class
        {
            var name = typeof(T).Name;
            if (name.EndsWith(OPTIONS_TAIL))
            {
                name = name.Substring(0, name.Length - OPTIONS_TAIL.Length);
            }


            var config = configuration.GetSection(name);

            collection.Configure<T>(config);

            return collection;
        }
    }
}
