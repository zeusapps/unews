using System;
using System.Text;
using System.Threading.Tasks;

namespace ZeusApps.News.Parser.Services
{
    public interface ICommunicationService
    {
        Task<T> Get<T>(Uri uri);

        Task<string> GetString(Uri uri, Encoding encoding = null);

        Task<T> Post<T>(Uri uri, object data);
    }
}