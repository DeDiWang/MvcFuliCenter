package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.MessageBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelAddCart extends IModelBase {
    void addCart(Context context,int goodsId,String userName,
                 int count,boolean isChecked,
                 OnCompleteListener<MessageBean> listener);
}
