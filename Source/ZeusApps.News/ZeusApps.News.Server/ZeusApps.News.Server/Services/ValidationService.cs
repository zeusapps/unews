using System;
using System.Linq;
using ZeusApps.News.Server.Models;
using ZeusApps.News.Server.Services.Abstraction;

namespace ZeusApps.News.Server.Services
{
    public class ValidationService: IValidationService
    {
        private readonly string[] _constaints = { "інтерфакс", "интерфакс" };

        public bool Validate(Article article)
        {
            if (string.IsNullOrEmpty(article.Html) || article.Html.Length < 75)
            {
                return false;
            }

            if (string.IsNullOrEmpty(article.Title))
            {
                return false;
            }

            return _constaints.All(x => article.Html.IndexOf(x, StringComparison.CurrentCultureIgnoreCase) == -1);
        }
    }
}
