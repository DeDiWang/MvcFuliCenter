package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.NewGoodsBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelCategory2Child extends IModelBase {
    void downloadCategory2Child(Context context, int catId, int pageId, OnCompleteListener<NewGoodsBean[]> listener);
}
