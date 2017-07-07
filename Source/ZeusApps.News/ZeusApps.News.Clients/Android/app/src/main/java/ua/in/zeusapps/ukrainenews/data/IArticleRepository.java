package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IArticleRepository {
    Single<List<Article>> getBySource(Source source);
    Single<List<Article>> getTopArticles();
    Single<Article> getById(String id);
    Single<Article> getNewest(Source source);
    Single<List<String>> getIds(String sourceKey);
    void create(Article article);
    void create(List<Article> articles);
    void removeBySource(Source source);
    void removeAll();
}
