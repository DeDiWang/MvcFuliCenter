package cn.ucai.mvcfulicenter.model.net;

import android.app.Notification;
import android.content.Context;

import cn.ucai.mvcfulicenter.bean.MessageBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelAddCollect extends IModelBase {
    void addCollectGood(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener);
}
