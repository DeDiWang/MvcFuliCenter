package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.CartBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelFindCarts extends IModelBase {
    void findCarts(Context context,String userName,OnCompleteListener<CartBean[]> listener);
}
