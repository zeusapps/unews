using System.Collections;
using System.Collections.Generic;

namespace ZeusApps.News.Server.Services.Abstraction
{
    public interface IMapperService
    {
        TDest Map<TDest>(object source);

        IEnumerable<TDest> Map<TDest>(IEnumerable source);

        TDestination Update<TSource, TDestination>(TSource source, TDestination destination);
    }
}
