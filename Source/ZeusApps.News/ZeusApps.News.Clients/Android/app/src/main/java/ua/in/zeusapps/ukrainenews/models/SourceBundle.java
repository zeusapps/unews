package ua.in.zeusapps.ukrainenews.models;

import java.util.List;

public class SourceBundle {
    private Source _source;
    private List<Article> _articles;

    public SourceBundle(Source source, List<Article> articles) {
        _source = source;
        _articles = articles;
    }

    public Source getSource() {
        return _source;
    }

    public List<Article> getArticles() {
        return _articles;
    }
}
