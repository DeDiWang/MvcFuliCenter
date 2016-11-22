package cn.ucai.mvcfulicenter.model.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
//import cn.ucai.mvcfulicenter.controller.activity.GoodsDetailsActivity;
import cn.ucai.mvcfulicenter.controller.activity.GoodsDetailsActivity;
import cn.ucai.mvcfulicenter.controller.activity.LoginActivity;
import cn.ucai.mvcfulicenter.controller.activity.MainActivity;
import cn.ucai.mvcfulicenter.controller.activity.RegisterActivity;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void gotoLoginActivity(Activity activity){
        startActivity(activity, LoginActivity.class);
    }
    public static void gotoRegisterActivity(Activity activity){
        startActivity(activity, RegisterActivity.class);
    }
    public static void startActivity(Context context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }
    public static void gotoDetailsActivity(Context context,int goodsId){
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
