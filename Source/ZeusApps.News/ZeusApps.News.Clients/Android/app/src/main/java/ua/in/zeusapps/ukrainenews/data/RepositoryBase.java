package ua.in.zeusapps.ukrainenews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class RepositoryBase<TItem, TKey> {
    private final RuntimeExceptionDao<TItem, TKey> _dao;

    private static List<Class> _childClasses = new ArrayList<>();

    private static int _databaseVersion = 2;

    private static String _databaseName = "data.db";

    RepositoryBase(Context context, Class<TItem> clazz) {
        Helper helper = new Helper(context);
        _dao = helper.getRuntimeExceptionDao(clazz);
    }

    public static void setDatebaseVersion(int databaseVersion){
        _databaseVersion = databaseVersion;
    }

    public static void setDatabaseName(String name){
        _databaseName = name;
    }

    public static void RegisterClass(Class clazz){
        _childClasses.add(clazz);
    }

    RuntimeExceptionDao<TItem, TKey> getDao(){
        return _dao;
    }

    private class Helper extends OrmLiteSqliteOpenHelper {
        Helper(Context context) {
            super(context, _databaseName, null, _databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {
                for (Class cls: _childClasses) {
                    TableUtils.dropTable(connectionSource, cls, true);
                    TableUtils.createTable(connectionSource, cls);
                }
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