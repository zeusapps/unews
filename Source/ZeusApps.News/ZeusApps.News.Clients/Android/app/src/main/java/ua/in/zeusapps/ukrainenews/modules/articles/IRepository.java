package ua.in.zeusapps.ukrainenews.modules.articles;

import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IRepository {
    void addArticle(Article article);

    void addAllArticles(List<Article> articles);

    List<Article> getAllArticles(String sourceId);

    void addAllSources(List<Source> sources);

    List<Source> getAllSources();

    void deleteAllSources(List<Source> sources);
}
