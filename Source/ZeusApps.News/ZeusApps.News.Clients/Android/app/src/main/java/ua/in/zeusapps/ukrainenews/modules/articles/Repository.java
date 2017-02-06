package ua.in.zeusapps.ukrainenews.modules.articles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.in.zeusapps.ukrainenews.models.Article;
import ua.in.zeusapps.ukrainenews.models.Source;


class Repository implements IRepository {
    private static final int ARTICLES_CACHE_COUNT = 50;

    private RuntimeExceptionDao<Article, String> _daoArticles;
    private RuntimeExceptionDao<Source, String> _daoSources;

    Repository(Context context) {
        Helper helper = new Helper(context);

        _daoArticles = helper.getRuntimeExceptionDao(Article.class);
        _daoSources = helper.getRuntimeExceptionDao(Source.class);
    }

    @Override
    public void addArticle(Article article){
        _daoArticles.create(article);
    }

    @Override
    public void addAllArticles(List<Article> articles){
        for (Article article: articles){
            addArticle(article);
        }

        if (articles.size() > 0){
            deleteArticles(articles.get(0).getSourceId());
        }
    }

    @Override
    public List<Article> getAllArticles(String sourceId){
        try {
            return _daoArticles
                    .queryBuilder()
                    .orderBy(Article.PUBLISHED_FIELD_NAME, false)
                    .where().eq(Article.SOURCE_ID_FIELD_NAME, sourceId)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addAllSources(List<Source> sources) {
        for (Source source: sources){
            _daoSources.create(source);
        }
    }

    @Override
    public List<Source> getAllSources() {
        return _daoSources.queryForAll();
    }

    @Override
    public Source getSourceByKey(String sourceId) {
        try {
            Source source = _daoSources
                    .queryBuilder()
                    .where()
                    .eq(Source.KEY_FIELD, sourceId)
                    .queryForFirst();
            return source;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteAllSources(List<Source> sources) {
        for (Source source: sources){
            try {
                _daoArticles
                        .deleteBuilder()
                        .where()
                        .eq(Article.SOURCE_ID_FIELD_NAME, source.getKey())
                        .query();
                _daoSources.delete(source);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteArticles(String sourceId){
        List<Article> articles = getAllArticles(sourceId);

        for (int i = 0; i < articles.size(); i++){
            if (i >= ARTICLES_CACHE_COUNT){
                _daoArticles.delete(articles.get(i));
            }
        }

    }

    private class Helper extends OrmLiteSqliteOpenHelper{
        private final static String DATABASE_NAME = "news.db";
        private final static int DATABASE_VERSION = 2;

        Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                TableUtils.createTable(connectionSource, Article.class);
                TableUtils.createTable(connectionSource, Source.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            try {
                TableUtils.dropTable(connectionSource, Article.class, true);
                onCreate(database, connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}