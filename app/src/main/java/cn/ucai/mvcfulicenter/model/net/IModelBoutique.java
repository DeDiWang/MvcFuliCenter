package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.BoutiqueBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelBoutique extends IModelBase {
    void downloadBoutinque(Context context, OnCompleteListener<BoutiqueBean[]> listener);
}
