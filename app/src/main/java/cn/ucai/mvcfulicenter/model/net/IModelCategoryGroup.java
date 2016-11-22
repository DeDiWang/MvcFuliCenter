package cn.ucai.mvcfulicenter.model.net;

import android.content.Context;

import cn.ucai.mvcfulicenter.bean.CategoryGroupBean;

/**
 * Created by 11039 on 2016/11/22.
 */

public interface IModelCategoryGroup extends IModelBase {
    void downloadCategoryGroupList(Context context, OnCompleteListener<CategoryGroupBean[]> listener);
}
