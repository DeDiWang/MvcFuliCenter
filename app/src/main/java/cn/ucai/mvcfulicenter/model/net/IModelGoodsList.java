package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.NewGoodsBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelGoodsList extends IModelBase {
    void downloadGoodsList(Context context, int catId, int pageId, OnCompleteListener<NewGoodsBean[]> listener);
}
