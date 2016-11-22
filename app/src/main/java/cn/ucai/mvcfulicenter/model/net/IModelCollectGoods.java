package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.CollectBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelCollectGoods extends IModelBase {
    void downloadCollectGoods(Context context, String userName, int pageId, OnCompleteListener<CollectBean[]> listener);
}
