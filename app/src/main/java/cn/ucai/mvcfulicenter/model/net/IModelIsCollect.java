package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.MessageBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelIsCollect extends IModelBase {
    void isCollect(Context context, int catId, String userName, OnCompleteListener<MessageBean> listener);
}
