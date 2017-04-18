package ua.in.zeusapps.ukrainenews.models;

public class ArticleRequestBundle {
    private Source _source;
    private Article _article;
    private boolean _isAfter;

    public ArticleRequestBundle(Source source, Article article, boolean isAfter) {
        _source = source;
        _article = article;
        _isAfter = isAfter;
    }

    public Source getSource() {
        return _source;
    }

    public Article getArticle() {
        return _article;
    }

    public boolean getIsAfter() {
        return _isAfter;
    }
}
