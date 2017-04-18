package ua.in.zeusapps.ukrainenews.models;

import java.util.List;

public class ArticleResponse {
    private final List<Article> _articles;
    private final boolean _refresh;

    public ArticleResponse(List<Article> articles, boolean refresh) {
        _articles = articles;
        _refresh = refresh;
    }

    public List<Article> getArticles(){
        return _articles;
    }

    public boolean getIsRefresh(){
        return _refresh;
    }
}
