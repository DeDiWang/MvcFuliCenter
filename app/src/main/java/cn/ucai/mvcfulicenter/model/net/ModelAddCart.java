package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelAddCart implements IModelAddCart {
    @Override
    public void addCart(Context context, int goodsId, String userName, int count, boolean isChecked, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,String.valueOf(goodsId))
                .addParam(I.Cart.USER_NAME,userName)
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(isChecked))
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
