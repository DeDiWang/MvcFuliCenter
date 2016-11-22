package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import java.io.File;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelUpdateAvatar extends IModelBase {
    void updateAvatar(Context context,String userName,File file,OnCompleteListener<String> listener);
}
