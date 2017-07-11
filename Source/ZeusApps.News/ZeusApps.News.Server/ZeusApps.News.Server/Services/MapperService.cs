using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using AutoMapper;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Services.Abstraction;

namespace ZeusApps.News.Server.Services
{
    public class MapperService : IMapperService
    {
        private static readonly DateTime Diff = new DateTime(1970, 1, 1, 0, 0, 0);
        private static IValidationService _validationService;

        static MapperService()
        {
            Mapper
                .Initialize(config =>
                {
                    config.CreateMap<Source, SourceDto>();
                    config.CreateMap<Article, ArticleDto>()
                        .AfterMap((article, dto) => dto.PublishedLong = (long)(dto.Published - Diff).TotalMilliseconds);
                    config.CreateMap<Article, ArticleVoteDto>();
                    config.CreateMap<Source, SourceDownloadableDto>();
                    config.CreateMap<ArticleDownloadableDto, Article>()
                        .AfterMap((dto, article) =>
                        {
                            article.Downloaded = DateTime.UtcNow;
                            article.IsClean = _validationService?.Validate(article) ?? false;
                        });
                });
        }

        public MapperService(IValidationService validationService)
        {
            _validationService = validationService;
        }

        public T Map<T>(object source)
        {
            return Mapper.Map<T>(source);
        }

        public IEnumerable<T> Map<T>(IEnumerable source)
        {
            return from object item in source select Map<T>(item);
        }

        public TDestination Update<TSource, TDestination>(
            TSource source, TDestination destination)
        {
            return Mapper.Map(source, destination);
        }
    }
}