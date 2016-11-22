package cn.ucai.mvcfulicenter.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.CategoryChildBean;
import cn.ucai.mvcfulicenter.bean.CategoryGroupBean;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;


/**
 * Created by 11039 on 2016/10/19.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context context,
                           ArrayList<CategoryGroupBean> groupList,
                           ArrayList<ArrayList<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList=new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList=new ArrayList<>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        if(mChildList!=null&&mChildList.get(i)!=null){
            return mChildList.get(i).size();
        }
        return 0;
    }

    @Override
    public CategoryGroupBean getGroup(int i) {
        if (mGroupList != null) {
            return mGroupList.get(i);
        }
        return null;
    }

    @Override
    public CategoryChildBean getChild(int i, int i1) {
        if (mChildList.get(i) != null && mChildList.get(i).get(i1) != null) {
            return mChildList.get(i).get(i1);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean groupBean = getGroup(groupPosition);
        if(groupBean!=null){
            ImageLoader.downloadImg(mContext, holder.ivGroupThumb, groupBean.getImageUrl());
            holder.tvGroupName.setText(groupBean.getName());
            if (isExpanded) {
                holder.ivIndicator.setImageResource(R.mipmap.expand_off);
            } else {
                holder.ivIndicator.setImageResource(R.mipmap.expand_on);
            }
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isExpanded, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        }else{
            holder= (ChildViewHolder) view.getTag();
        }
        CategoryChildBean childBean = getChild(i,i1);
        if(childBean!=null) {
            holder.tvChildName.setText(childBean.getName());
            ImageLoader.downloadImg(mContext, holder.ivChildThumb, childBean.getImageUrl());
            /*//向列表监听项传入该ChildBean对象
            holder.layoutCategoryChild.setTag(childBean);*/
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void addAll(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        mGroupList.clear();
        mGroupList.addAll(groupList);
        mChildList.clear();
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        @BindView(R.id.ivGroupThumb)
        ImageView ivGroupThumb;
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.ivIndicator)
        ImageView ivIndicator;
        @BindView(R.id.layout_category_group)
        RelativeLayout layoutCategoryGroup;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.ivChildThumb)
        ImageView ivChildThumb;
        @BindView(R.id.tvChildName)
        TextView tvChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
       /* //小类列表项监听事件
        @OnClick(R.id.layout_category_child)
        public void onCategoryChildItemOnClick(){
            CategoryChildBean childBean = (CategoryChildBean) layoutCategoryChild.getTag();
            mContext.startActivity(new Intent(mContext, CategoryActivity.class).
                    putExtra("childBean",childBean));
        }*/
    }
}
