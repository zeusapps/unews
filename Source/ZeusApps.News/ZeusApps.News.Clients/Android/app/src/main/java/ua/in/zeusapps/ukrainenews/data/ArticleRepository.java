package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class ArticleRepository
        extends RepositoryBase<Article, String>
        implements IArticleRepository {

    ArticleRepository(Context context) {
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
}
