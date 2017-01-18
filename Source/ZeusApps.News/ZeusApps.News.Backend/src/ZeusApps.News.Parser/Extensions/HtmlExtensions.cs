using System;
using HtmlAgilityPack;

namespace ZeusApps.News.Parser.Extensions
{
    public static class HtmlExtensions
    {
        public static HtmlNode Remove(this HtmlNode node, string xpath)
        {
            var items = node?.SelectNodes(xpath);
            if (items == null)
            {
                return node;
            }

            foreach (var item in items)
            {
                item.Remove();
            }

            return node;
        }

        public static HtmlNode RemoveStyles(this HtmlNode node)
        {
            var items = node?.SelectNodes("//*[@style]");
            if (items == null)
            {
                return node;
            }

            foreach (var item in items)
            {
                item.Attributes["style"].Remove();
            }

            return node;
        }

        public static HtmlNode UpdateSource(this HtmlNode node, string baseUrl)
        {
            var items = node?.SelectNodes("//*[@src]");
            if (items == null)
            {
                return node;
            }

            foreach (var item in items)
            {
                var url = item.Attributes["src"].Value;
                if (url.StartsWith("/") && !url.StartsWith("//"))
                {
                    item.Attributes["src"].Value = new Uri(new Uri(baseUrl), url).ToString();
                }
            }


            return node;
        }
    }
}
