namespace ZeusApps.News.Repository.Mongo.Options
{
    public class ConnectionStringsOption
    {
        public const string Key = "ConnectionStrings";

        public string Path { get; set; }

        public string Database { get; set; }
    }
}
