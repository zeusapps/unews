package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IArticleRepository {
    Observable<List<Article>> getAll();
    Observable<List<Article>> getBySource(Source source);
    Observable<Article> getById(String id);
    void create(Article article);
    void removeBySource(Source source);
    void removeAll();
}
