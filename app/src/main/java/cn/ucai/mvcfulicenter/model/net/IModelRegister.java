package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.Result;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelRegister extends IModelBase{
    void register(Context context, String userName, String userNick, String password, OnCompleteListener<Result> listener);
}
