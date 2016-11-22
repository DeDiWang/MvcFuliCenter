package cn.ucai.mvcfulicenter.controller.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.SharedPreferencesUtils;
import cn.ucai.mvcfulicenter.model.db.UserDao;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private long splashTime=1500;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context=SplashActivity.this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                //create dbs
                long castTime = System.currentTimeMillis() - currentTime;
                if(castTime<splashTime){
                    try {
                        Thread.sleep(splashTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //startActivity(new Intent(SplashActivity.this,MainActivity.class));
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        }).start();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //从内存中取数据，若没有则从数据库中取
                UserAvatar user = FuLiCenterApplication.getUser();
                L.e(TAG+",从内存中取的用户信息=="+user);
                //从首选项中读取用户名
                String userName = SharedPreferencesUtils.getInstance(context).getUser();
                L.e(TAG+",从首选项中取的用户名=="+userName);
                if(user==null && userName!=null){
                    UserDao dao = new UserDao(context);
                    user = dao.getUser(userName);
                    L.e(TAG+"从数据库中取的用户信息=="+user);
                    if(user!=null){
                        FuLiCenterApplication.setUser(user);
                    }
                }
                MFGT.gotoMainActivity(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        },splashTime);
    }
}
