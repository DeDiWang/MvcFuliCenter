package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;
import android.database.CursorJoiner;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.Result;
import cn.ucai.mvcfulicenter.model.utils.MD5;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelRegister implements IModelRegister {


    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }

    @Override
    public void register(Context context, String userName, String nick, String password, OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }
}
