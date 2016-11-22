package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelUpdateAvatar implements IModelUpdateAvatar {
    @Override
    public void updateAvatar(Context context, String muserName, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,muserName)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }
}
