using System;

namespace ZeusApps.News.Server.Parameters
{
    public class ArticlesParameters
    {
        public string SourceId { get; set; }

        public int Count { get; set; } = 20;

        public DateTime? Published { get; set; }

        public bool IsAfter { get; set; }
    }
}
