package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.MessageBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelDeleteCollect extends IModelBase {
    void deleteCollect(Context context, int goodsId, String userName, OnCompleteListener<MessageBean> listener);
}
