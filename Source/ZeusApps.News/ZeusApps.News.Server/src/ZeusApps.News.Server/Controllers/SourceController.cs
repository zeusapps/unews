using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Repositories;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Services;

// For more information on enabling Web API for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace ZeusApps.News.Server.Controllers
{
    [Route("api/[controller]")]
    public class SourceController : Controller
    {
        private readonly ISourceRepository _repository;
        private readonly IMapperService _mapperService;
        private readonly ILogger<SourceController> _logger;

        public SourceController(
            ILoggerFactory loggerFactory,
            ISourceRepository repository,
            IMapperService mapperService)
        {
            _logger = loggerFactory.CreateLogger<SourceController>();
            _repository = repository;
            _mapperService = mapperService;
        }

        // GET: api/values
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var sources = await _repository.GetReadableSources();
            _logger.LogInformation($"Readable sources: {sources.Length}");
            return Ok(_mapperService.Map<SourceDto>(sources));
        }
    }
}
