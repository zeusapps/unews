using ZeusApps.News.Server.Models;

namespace ZeusApps.News.Server.Services.Abstraction
{
    public interface IValidationService
    {
        bool Validate(Article article);
    }
}
