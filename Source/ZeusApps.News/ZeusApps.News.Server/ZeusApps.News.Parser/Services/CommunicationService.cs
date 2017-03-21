using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using ZeusApps.News.Parser.Options;

namespace ZeusApps.News.Parser.Services
{
    public class CommunicationService : ICommunicationService
    {
        private const string CONTENT_TYPE_JSON = "application/json";
        private readonly ILogger<CommunicationService> _logger;
        private readonly HttpClient _httpClient;

        public CommunicationService(
            IOptions<ExecuteOptions> executeOptions,
            ILogger<CommunicationService> logger)
        {
            _logger = logger;
            _httpClient = new HttpClient
            {
                BaseAddress = new Uri(executeOptions.Value.Host),
                DefaultRequestHeaders =
                {
                    Accept = { MediaTypeWithQualityHeaderValue.Parse(CONTENT_TYPE_JSON)}
                }
            };
        }

        public async Task<T> Get<T>(Uri uri)
        {
            try
            {
                var json = await _httpClient.GetStringAsync(uri);
                return JsonConvert.DeserializeObject<T>(json);
            }
            catch (Exception e)
            {
                _logger.LogError(1, e, e.Message);
                return default(T);
            }
        }

        public async Task<T> Post<T>(Uri uri, object data)
        {
            try
            {
                var json = JsonConvert.SerializeObject(data);
                var response = await _httpClient.PostAsync(uri, new StringContent(json, Encoding.UTF8, CONTENT_TYPE_JSON));
                response.EnsureSuccessStatusCode();

                json = await response.Content.ReadAsStringAsync();

                return JsonConvert.DeserializeObject<T>(json);
            }
            catch (Exception e)
            {
                _logger.LogError(1, e, e.Message);
                return default(T);
            }
        }

        public async Task<string> GetString(Uri uri, Encoding encoding = null)
        {
            if (encoding == null)
            {
                return await _httpClient.GetStringAsync(uri);
            }
            
            var data = await _httpClient.GetByteArrayAsync(uri);
            var text = encoding.GetString(data);
            return text;
        }
    }
}
