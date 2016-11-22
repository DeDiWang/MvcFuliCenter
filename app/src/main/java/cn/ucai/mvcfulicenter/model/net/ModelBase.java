package cn.ucai.mvcfulicenter.model.net;

import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelBase implements IModelBase {
    @Override
    public void release() {
        OkHttpUtils.release();
    }

    @Override
    public void releaseImage() {
        ImageLoader.release();
    }
}
