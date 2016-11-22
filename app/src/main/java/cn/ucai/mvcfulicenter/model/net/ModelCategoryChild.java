package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.bean.CategoryChildBean;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;

/**
 * Created by 11039 on 2016/11/22.
 */

public class ModelCategoryChild implements IModelCategoryChild {
    @Override
    public void downloadCategoryChildList(Context context,int parentId, OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,parentId+"")
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    @Override
    public void release() {

    }

    @Override
    public void releaseImage() {

    }
}
