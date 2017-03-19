using System.Collections.Generic;

namespace ZeusApps.News.Parser.Services
{
    public interface IDependencyResolverService
    {
        T ResolveService<T>();
        IEnumerable<T> ResolveServices<T>();
    }

    public class DependencyResolverService : IDependencyResolverService
    {
        public T ResolveService<T>() => Program.ResolveService<T>();

        public IEnumerable<T> ResolveServices<T>() => Program.ResolveServices<T>();
    }
}
