using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using ZeusApps.News.Repositories;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Services;

// For more information on enabling Web API for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace ZeusApps.News.Server.Controllers
{
    [Route("api/[controller]")]
    public class ArticleController : Controller
    {
        private readonly IArticleRepository _repository;
        private readonly IMapperService _mapperService;

        public ArticleController(
            IArticleRepository repository,
            IMapperService mapperService)
        {
            _repository = repository;
            _mapperService = mapperService;
        }

        [HttpGet("{sourceId}")]
        public async Task<IActionResult> Get(string sourceId)
        {
            if (string.IsNullOrEmpty(sourceId))
            {
                return BadRequest("sourceId couldn't be null or empty");
            }

            var articles = await _repository.GetArticle(sourceId);
            return Ok(_mapperService.Map<ArticleDto>(articles));
        }

        [HttpGet("{articleId}/votes")]
        public async Task<IActionResult> GetVotes(string articleId)
        {
            var article = await _repository.GetArticle(articleId);
            if (article == null)
            {
                return NotFound();
            }

            return Ok(_mapperService.Map<ArticleVoteDto>(article));
        }

        [HttpPost("{articleId}/upvote")]
        public async Task<IActionResult> Upvote(string articleId)
        {
            var result = await _repository.Upvote(articleId);

            if (result)
            {
                return Accepted();
            }
            return BadRequest();
        }

        [HttpPost("{articleId}/downvote")]
        public async Task<IActionResult> Downvote(string articleId)
        {
            var result = await _repository.Downvote(articleId);

            if (result)
            {
                return Accepted();
            }
            return BadRequest();
        }
    }
}
