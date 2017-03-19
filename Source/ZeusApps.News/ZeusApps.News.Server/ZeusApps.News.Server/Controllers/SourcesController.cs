using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using ZeusApps.News.Server.DTOs;
using ZeusApps.News.Server.Repositories.Abstraction;
using ZeusApps.News.Server.Services.Abstraction;

namespace ZeusApps.News.Server.Controllers
{
    [Route("api/sources")]
    public class SourcesController : Controller
    {
        private readonly ISourceRepository _repository;
        private readonly IMapperService _mapperService;
        private readonly ILogger<SourcesController> _logger;

        public SourcesController(
            ILoggerFactory loggerFactory,
            ISourceRepository repository,
            IMapperService mapperService)
        {
            _logger = loggerFactory.CreateLogger<SourcesController>();
            _repository = repository;
            _mapperService = mapperService;
        }
        
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var sources = await _repository.GetReadableSources();
            _logger.LogInformation($"Readable sources: {sources.Length}");

            var sourcesDtos = _mapperService.Map<SourceDto>(sources);
            return Ok(sourcesDtos);
        }

        [HttpGet("downloadable")]
        public async Task<IActionResult> GetDownloadable()
        {
            var sources = await _repository.GetDownloadableSources();
            _logger.LogInformation($"Downloadable sources: {sources.Length}");

            var sourcesDtos = _mapperService.Map<SourceDownloadableDto>(sources);
            return Ok(sourcesDtos);
        }
    }
}
