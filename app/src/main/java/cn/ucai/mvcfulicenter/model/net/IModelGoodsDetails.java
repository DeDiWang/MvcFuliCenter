package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.GoodsDetailsBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelGoodsDetails extends IModelBase {
    void downloadGoodsDetails(Context context, int goodsId, OnCompleteListener<GoodsDetailsBean> listener);
}
