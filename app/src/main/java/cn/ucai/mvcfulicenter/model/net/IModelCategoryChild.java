package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.CategoryChildBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelCategoryChild extends IModelBase {
    void downloadCategoryChildList(Context context,int parentId, OnCompleteListener<CategoryChildBean[]> listener);
}
