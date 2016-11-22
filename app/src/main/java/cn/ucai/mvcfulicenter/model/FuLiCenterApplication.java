package cn.ucai.mvcfulicenter.model;


import android.app.Application;
import android.content.Context;

import cn.ucai.mvcfulicenter.bean.UserAvatar;

/**
 * Created by 11039 on 2016/11/21.
 */

public class FuLiCenterApplication extends Application {
    public static Context application;
    private static FuLiCenterApplication instance;
    private static String userName;
    private static UserAvatar user;

    public static UserAvatar getUser() {
        return user;
    }

    public static void setUser(UserAvatar user) {
        FuLiCenterApplication.user = user;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FuLiCenterApplication.userName = userName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }
    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }

}
