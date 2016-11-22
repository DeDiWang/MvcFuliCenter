package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelLogin extends IModelBase{
    void login(Context context, String userName, String password, OnCompleteListener<String> listener);
}
