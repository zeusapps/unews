package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class ArticleRepository
        extends RepositoryBase<Article, String>
        implements IArticleRepository {

    ArticleRepository(Context context) {
        super(context, Article.class);
    }

    @Override
    public Observable<List<Article>> getBySource(Source source) {
        List<Article> articles;

        try {
            QueryBuilder<Article, String> builder = getDao().queryBuilder();
            builder.where().eq(Article.SOURCE_ID_FIELD_NAME, source.getKey());

            articles = builder
                    .orderBy(Article.PUBLISHED_FIELD_NAME, false)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
            articles = new ArrayList<>();
        }

        return Observable.just(articles);
    }

    @Override
    public Observable<Article> getById(String id) {
        Article article = getDao().queryForId(id);
        return Observable.just(article);
    }

    @Override
    public Observable<List<String>> getIds(Source source) {
        return getBySource(source)
                .map(new Func1<List<Article>, List<String>>() {
                    @Override
                    public List<String> call(List<Article> articles) {
                        List<String> ids = new ArrayList<>();

                        for (Article article: articles){
                            ids.add(article.getId());
                        }

                        return ids;
                    }
                });
    }

    @Override
    public void create(Article article){
        try {
            Article localArticle = getDao().queryBuilder()
                    .where()
                    .eq(Article.ID_FIELD_NAME, article.getId())
                    .queryForFirst();

            if (localArticle == null){
                getDao().create(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBySource(Source source) {
        try {
            DeleteBuilder<Article, String> builder = getDao()
                    .deleteBuilder();
            builder.where().eq(Article.SOURCE_ID_FIELD_NAME, source.getKey());
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        DeleteBuilder<Article, String> builder = getDao().deleteBuilder();
        try {
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
