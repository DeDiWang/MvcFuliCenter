package cn.ucai.mvcfulicenter.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.mvcfulicenter.bean.UserAvatar;

/**
 * Created by 11039 on 2016/11/22.
 */

public class DBManager  {
    private static DBManager instance = new DBManager();
    private static DBOpenHelper mHelper;
    void onInit(Context context){
        mHelper =new DBOpenHelper(context);
    }
    public static synchronized DBManager getInstance(){
        return instance;
    }
    public void closeDB(){
        if(mHelper!=null){
            mHelper.closeDB();
        }
    }

    public static synchronized boolean saveUser(UserAvatar user){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());
        if(db.isOpen()){
            return db.replace(UserDao.TABLE_USER_NAME,null,values)!=-1;
        }
        return false;
    }

    public static synchronized UserAvatar getUser(String userName){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from "+UserDao.TABLE_USER_NAME+" where "+UserDao.USER_COLUMN_NAME +" =?";
        Cursor c = db.rawQuery(sql, new String[]{userName});
        while (c.moveToNext()){
            UserAvatar user = new UserAvatar();
            user.setMuserName(userName);
            user.setMuserNick(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(c.getInt(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(c.getInt(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(c.getString(c.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;
        }
        return null;
    }

    public static synchronized boolean updateUser(UserAvatar user){
        int result = -1;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        String where = UserDao.USER_COLUMN_NAME+" =?";
        if(db.isOpen()){
            result = db.update(UserDao.TABLE_USER_NAME,values,where,new String[]{user.getMuserName()});
        }
        return result>0;
    }
}
