using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Core.DTOs;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Repositories.Abstraction;
using ZeusApps.News.Server.Services.Abstraction;

namespace ZeusApps.News.Server.Controllers
{
    [Route("api/sources/{sourceId}/articles")]
    public class ArticlesController : Controller
    {
        private readonly ILogger<ArticlesController> _logger;
        private readonly IArticleRepository _repository;
        private readonly IMapperService _mapperService;

        public ArticlesController(
            ILoggerFactory loggerFactory,
            IArticleRepository repository,
            IMapperService mapperService)
        {
            _logger = loggerFactory.CreateLogger<ArticlesController>();
            _repository = repository;
            _mapperService = mapperService;
        }

        [HttpGet]
        public async Task<IActionResult> Get(string sourceId, int count = 20, int offset = 0, DateTime? published = null, bool isAfter = false)
        {
            if (string.IsNullOrEmpty(sourceId))
            {
                _logger.LogInformation("sourceId is empty");
                return BadRequest("sourceId couldn't be null or empty");
            }

            var articles = await _repository.GetArticles(sourceId, count, offset, published, isAfter);
            _logger.LogInformation($"Articles: {articles.Length}");
            return Ok(_mapperService.Map<ArticleDto>(articles));
        }

        [HttpPost]
        public async Task<IActionResult> Create(string sourceId, [FromBody] ArticleDownloadableDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }
            
            var article = _mapperService.Map<Article>(dto);
            article.IsClean = ValidateArtile(article);

            await _repository.AddArticle(article);

            //TODO add Created 
            return NoContent();
        }

        [HttpPost("contains")]
        public async Task<IActionResult> ContainsArticle(string sourceId, [FromBody] ArticleContainsDto article)
        {
            var result = await _repository.ContainsGuid(sourceId, article.Guid);
            return Ok(result);
        }


        [HttpGet("{articleId}/votes")]
        public async Task<IActionResult> GetVotes(string articleId)
        {
            var article = await _repository.GetArticle(articleId);
            if (article == null)
            {
                _logger.LogInformation("Article not found");
                return NotFound();
            }

            _logger.LogInformation("Article found", article);
            return Ok(_mapperService.Map<ArticleVoteDto>(article));
        }

        [HttpPost("{articleId}/upvote")]
        public async Task<IActionResult> Upvote(string articleId)
        {
            var result = await _repository.Upvote(articleId);

            if (result)
            {
                _logger.LogInformation($"Article {articleId} upvoted");
                return Accepted();
            }

            _logger.LogInformation($"Article {articleId} not found");
            return NotFound();
        }

        [HttpPost("{articleId}/downvote")]
        public async Task<IActionResult> Downvote(string articleId)
        {
            var result = await _repository.Downvote(articleId);

            if (result)
            {
                _logger.LogInformation($"Article {articleId} downvoted");
                return Accepted();
            }

            _logger.LogInformation($"Article {articleId} not found");
            return BadRequest();
        }

        private static bool ValidateArtile(Article article)
        {
            if (string.IsNullOrEmpty(article.Html) || article.Html.Length < 75)
            {
                return false;
            }

            if (string.IsNullOrEmpty(article.Title))
            {
                return false;
            }

            var constraints = new[] {"Інтерфакс", "Интерфакс"};
            return !constraints.Any(x => article.Html.Contains(x));
        }
    }

}
