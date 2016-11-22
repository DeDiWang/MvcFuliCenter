package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelCollectCount implements IModelCollectCount {
    @Override
    public void findCollectCount(Context context, String userName, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }
}
