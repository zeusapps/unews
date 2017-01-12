using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using AutoMapper;
using ZeusApps.News.Models;
using ZeusApps.News.Server.DTOs;

namespace ZeusApps.News.Server.Services
{
    public interface IMapperService
    {
        TDest Map<TDest>(object source);

        IEnumerable<T> Map<T>(IEnumerable source);
    }
    public class MapperService : IMapperService
    {
        public MapperService()
        {
            Mapper
                .Initialize(config =>
                {
                    config.CreateMap<Source, SourceDto>();
                });
        }

        public T Map<T>(object source)
        {
            return Mapper.Map<T>(source);
        }

        public IEnumerable<T> Map<T>(IEnumerable source)
        {
            return from object item in source select Map<T>(item);
        }
    }
}
