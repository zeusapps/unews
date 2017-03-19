namespace ZeusApps.News.Server.Models
{
    public class Source
    {
        public string Id { get; set; }

        public string Title { get; set; }

        public string Encoding { get; set; }

        public string BaseUrl { get; set; }

        public string ImageUrl { get; set; }

        public string SourceUrl { get; set; }

        public string Key { get; set; }

        public bool IsDownloadable { get; set; }

        public bool IsReadable { get; set; }
    }
}
