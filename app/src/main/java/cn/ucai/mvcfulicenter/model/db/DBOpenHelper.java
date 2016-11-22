package cn.ucai.mvcfulicenter.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.mvcfulicenter.I;

/**
 * Created by 11039 on 2016/11/22.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static DBOpenHelper instance;
    private static final int VERSION = 1;
    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            +UserDao.TABLE_USER_NAME + " ("
            +UserDao.USER_COLUMN_NAME + " TEXT PRIMARY KEY, "
            +UserDao.USER_COLUMN_NICK + " TEXT, "
            +UserDao.USER_COLUMN_AVATAR_ID +" INTEGER, "
            +UserDao.USER_COLUMN_AVATAR_TYPE +" INTEGER, "
            +UserDao.USER_COLUMN_AVATAR_PATH +" TEXT, "
            +UserDao.USER_COLUMN_AVATAR_SUFFIX +" TEXT, "
            +UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME +" TEXT);";
    public DBOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    public DBOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, VERSION);
    }

    public static String getUserDatabaseName(){
        return I.User.TABLE_NAME+"_demo.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB(){
        if(instance!=null){
            SQLiteDatabase database = getWritableDatabase();
            database.close();
            instance = null;
        }
    }
}
