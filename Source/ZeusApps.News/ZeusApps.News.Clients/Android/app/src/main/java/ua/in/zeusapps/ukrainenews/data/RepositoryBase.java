package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

abstract class RepositoryBase<TItem, TKey> {
    private final RuntimeExceptionDao<TItem, TKey> _dao;

    RepositoryBase(Context context, Class<TItem> clazz) {
        Helper helper = new Helper(context, clazz);
        _dao = helper.getRuntimeExceptionDao(clazz);
    }

    RuntimeExceptionDao<TItem, TKey> getDao(){
        return _dao;
    }

    private class Helper extends OrmLiteSqliteOpenHelper {
        private final static String DATABASE_NAME = "news.db";
        private final static int DATABASE_VERSION = 4;
        private final Class<TItem> itemClass;

        Helper(Context context, Class<TItem> cls) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            itemClass = cls;
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                TableUtils.dropTable(connectionSource, itemClass, true);
                TableUtils.createTable(connectionSource, itemClass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
            onCreate(database, connectionSource);
        }
    }

}