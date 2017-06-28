package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;

class ArticleRepository
        extends RepositoryBase<Article, String>
        implements IArticleRepository {

    ArticleRepository(Context context) {
        super(context, Article.class);
    }

    @Override
    public Single<List<Article>> getBySource(Source source) {
        return Single.fromCallable(() -> {
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

            return articles;
        });
    }

    @Override
    public Single<Article> getById(String id) {
        return Single.fromCallable(() -> getDao().queryForId(id));
    }

    @Override
    public Single<Article> getNewest(Source source) {
        return Single.fromCallable(() -> {
            QueryBuilder<Article, String> builder = getDao().queryBuilder();
            builder.where().eq(Article.SOURCE_ID_FIELD_NAME, source.getKey());
            builder.orderBy(Article.PUBLISHED_FIELD_NAME, false);
            return builder.queryForFirst();
        });
    }

    @Override
    public Single<List<String>> getIds(Source source) {
        return getBySource(source)
            .map(articles -> {
                List<String> ids = new ArrayList<>();

                for (Article article: articles) {
                    ids.add(article.getId());
                }

                return ids;
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
    public void create(List<Article> articles) {
        for (Article article: articles) {
            create(article);
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
