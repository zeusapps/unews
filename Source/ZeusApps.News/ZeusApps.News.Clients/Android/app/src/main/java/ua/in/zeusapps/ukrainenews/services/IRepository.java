package ua.in.zeusapps.ukrainenews.services;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IRepository {
    void addArticle(Article article);

    void addAllArticles(List<Article> articles);

    List<Article> getAllArticles(String sourceId);

    Article getArticle(String articleId);

    void deleteAlArticles();

    void addAllSources(List<Source> sources);

    List<Source> getAllSources();

    Source getSourceByKey(String sourceId);

    void deleteAllSources(List<Source> sources);
}
