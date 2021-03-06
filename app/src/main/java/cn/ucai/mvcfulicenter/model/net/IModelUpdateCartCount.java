package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.MessageBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelUpdateCartCount extends IModelBase {
    void updateCartCount(Context context, int id, int count, boolean isChecked,
                         OnCompleteListener<MessageBean> listener);
}
