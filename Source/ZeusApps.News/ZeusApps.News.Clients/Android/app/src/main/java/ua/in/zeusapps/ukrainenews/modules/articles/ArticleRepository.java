package ua.in.zeusapps.ukrainenews.modules.articles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ua.in.zeusapps.ukrainenews.models.Article;


public class ArticleRepository {
    private RuntimeExceptionDao<Article, String> _dao;

    public ArticleRepository(Context context) throws SQLException {
        _dao = new Helper(context).getDao(Article.class);
    }

    public void add(Article article){
        _dao.create(article);
    }

    private class Helper extends OrmLiteSqliteOpenHelper{
        private final static String DATABASE_NAME = "news.db";
        private final static int DATABASE_VERSION = 1;

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                TableUtils.createTable(connectionSource, Article.class);
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
