using System;

namespace ZeusApps.News.Server.Parameters
{
    public class ArticlesParameters
    {
        private DateTime? _published;

        public string SourceId { get; set; }

        public int Count { get; set; } = 20;

        public DateTime? Published
        {
            get => Timestamp > 0 ? DateTimeOffset.FromUnixTimeMilliseconds(Timestamp).DateTime : _published;
            set => _published = value;
        }

        public long Timestamp { get; set; }

        public bool IsAfter { get; set; }
    }
}
