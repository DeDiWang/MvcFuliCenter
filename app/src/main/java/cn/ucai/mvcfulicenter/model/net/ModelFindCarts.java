package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.CartBean;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelFindCarts implements IModelFindCarts {
    @Override
    public void findCarts(Context context, String userName, OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }
}
