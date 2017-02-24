using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Repositories;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Services;

namespace ZeusApps.News.Server.Controllers
{
    [Route("api/[controller]")]
    public class ArticleController : Controller
    {
        private readonly ILogger<ArticleController> _logger;
        private readonly IArticleRepository _repository;
        private readonly IMapperService _mapperService;

        public ArticleController(
            ILoggerFactory loggerFactory,
            IArticleRepository repository,
            IMapperService mapperService)
        {
            _logger = loggerFactory.CreateLogger<ArticleController>();
            _repository = repository;
            _mapperService = mapperService;
        }

        [HttpGet("{sourceId}")]
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
    }
}
