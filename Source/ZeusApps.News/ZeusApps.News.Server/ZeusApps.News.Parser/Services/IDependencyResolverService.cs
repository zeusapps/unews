using System.Collections.Generic;

namespace ZeusApps.News.Parser.Services
{
    public interface IDependencyResolverService
    {
        T ResolveService<T>();
        T ResolveService<T>(string name);
        IEnumerable<T> ResolveServices<T>();
    }
}