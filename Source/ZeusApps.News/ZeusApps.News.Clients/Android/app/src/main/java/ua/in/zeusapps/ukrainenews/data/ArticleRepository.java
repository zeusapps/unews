package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

public class ArticleRepository
        extends RepositoryBase<Article, String>
        implements IArticleRepository {

    public ArticleRepository(Context context) {
        super(context, Article.class);
    }

    @Override
    public Observable<List<Article>> getAll() {
        List<Article> articles = getDao().queryForAll();

        return Observable.just(articles);
    }

    @Override
    public Observable<List<Article>> getBySource(Source source) {
        List<Article> articles;

        try {
            articles = getDao()
                    .queryBuilder()
                    .orderBy(Article.PUBLISHED_FIELD_NAME, false)
                    .where()
                    .eq(Article.SOURCE_ID_FIELD_NAME, source.getKey())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
            articles = new ArrayList<>();
        }

        return Observable.just(articles);
    }

    @Override
    public void create(Article article){
        getDao().create(article);
    }
}
