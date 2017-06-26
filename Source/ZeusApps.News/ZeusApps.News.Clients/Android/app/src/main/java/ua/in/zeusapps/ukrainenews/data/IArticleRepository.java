package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IArticleRepository {
    Single<List<Article>> getBySource(Source source);
    Single<Article> getById(String id);
    Single<Article> getNewest(Source source);
    Single<List<String>> getIds(Source source);
    void create(Article article);
    void removeBySource(Source source);
    void removeAll();
}
