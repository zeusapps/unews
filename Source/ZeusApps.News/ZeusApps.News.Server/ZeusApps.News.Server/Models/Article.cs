﻿using System;

namespace ZeusApps.News.Server.Models
{
    public class Article
    {
        public string Id { get; set; }

        public string SourceId { get; set; }

        public string Title { get; set; }

        public string Html { get; set; }

        public string OriginalHtml { get; set; }

        public string Url { get; set; }

        public string ImageUrl { get; set; }

        public string FullText { get; set; }

        public string Description { get; set; }

        public string Category { get; set; }

        public string Guid { get; set; }

        public string Author { get; set; }

        public string Image { get; set; }

        public DateTime Published { get; set; }

        public DateTime Downloaded { get; set; }

        public bool IsClean { get; set; }

        public int Upvote { get; set; }

        public int Downvote { get; set; }
    }
}
