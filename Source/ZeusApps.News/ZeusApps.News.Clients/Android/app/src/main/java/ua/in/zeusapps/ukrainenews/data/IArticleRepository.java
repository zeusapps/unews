package ua.in.zeusapps.ukrainenews.data;

import java.util.List;

import io.reactivex.Observable;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public interface IArticleRepository {
    Observable<List<Article>> getBySource(Source source);
    Observable<Article> getById(String id);
    Observable<List<String>> getIds(Source source);
    void create(Article article);
    void removeBySource(Source source);
    void removeAll();
}
