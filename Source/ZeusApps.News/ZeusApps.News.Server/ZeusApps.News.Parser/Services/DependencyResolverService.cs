using System;
using System.Collections.Generic;
using System.Linq;

namespace ZeusApps.News.Parser.Services
{
    public class DependencyResolverService : IDependencyResolverService
    {
        public T ResolveService<T>() => Program.ResolveService<T>();

        public IEnumerable<T> ResolveServices<T>() => Program.ResolveServices<T>();

        public T ResolveService<T>(string name) =>
            ResolveServices<T>()
                .FirstOrDefault(x => 
                    x.GetType()
                        .Name
                        .StartsWith(name, StringComparison.CurrentCultureIgnoreCase));
    }
}
