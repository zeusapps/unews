using System;
using System.Linq;
using System.Xml;
using System.Xml.Linq;

namespace ZeusApps.News.Parser.Models
{
    public class RssItem
    {
        public XmlNode Node { get; }
        public XmlNode TitleNode => GetNode("title");
        public XmlNode LinkNode => GetNode("link");
        public XmlNode DescriptionNode => GetNode("description");
        public XmlNode FullTextNode => GetNode("fulltext");
        public XmlNode PubDateNode => GetNode("pubDate");
        public XmlNode CategoryNode => GetNode("category");
        public XmlNode GuidNode => GetNode("guid");
        public XmlNode EnclosureNode => GetNode("enclosure");
        public XmlNode AuthorNode => GetNode("author");
        public XmlNode ImageNode => GetNode("image");


        public string Title => TitleNode?.InnerText;
        public string Link => LinkNode?.InnerText;
        public string Description => DescriptionNode?.InnerText;
        public DateTime? PubDate
        {
            get
            {
                if (PubDateNode == null)
                    return new DateTime?();

                DateTime dateTime;
                return DateTime.TryParse(PubDateNode.InnerText, out dateTime) 
                    ? dateTime 
                    : new DateTime?();
            }
        }
        public string Category => CategoryNode?.InnerText;
        public string Guid => GuidNode?.InnerText;
        public string Enclosure => EnclosureNode?.InnerText;
        public string EnclosureImage => GetImage();
        public string FullText => FullTextNode?.InnerText;
        public string Author => AuthorNode?.InnerText;
        public string Image => ImageNode?.InnerText;

        public RssItem(XmlNode xmlNode)
        {
            Node = xmlNode;
        }

        public override string ToString()
        {
            return $"{PubDate}:{Title}";
        }

        private XmlNode GetNode(string localName)
        {
            return Node
                .ChildNodes
                .Cast<XmlNode>()
                .FirstOrDefault(xmlNode => xmlNode.LocalName == localName);
        }

        private string GetImage()
        {
            var e = EnclosureNode;
            if (e == null)
            {
                return null;
            }

            try
            {
                return e.Attributes["url"].InnerText;
            }
            catch
            {
                return null;
            }
        }
    }
}
