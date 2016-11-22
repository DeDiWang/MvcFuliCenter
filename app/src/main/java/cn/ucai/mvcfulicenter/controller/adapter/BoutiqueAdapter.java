package cn.ucai.mvcfulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.BoutiqueBean;
import cn.ucai.mvcfulicenter.controller.activity.Boutique2Activity;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.L;

/**
 * Created by 11039 on 2016/10/18.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<BoutiqueBean> mBoutiqueList;

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> boutiqueList) {
        this.context = context;
        this.mBoutiqueList = boutiqueList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = new BoutiqueViewHolder(inflater.inflate(R.layout.item_boutique, null));
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueBean boutiqueBeen = mBoutiqueList.get(position);
        BoutiqueViewHolder boutiqueViewHolder= (BoutiqueViewHolder) holder;
        boutiqueViewHolder.tvBoutiqueTitle.setText(boutiqueBeen.getTitle());
        boutiqueViewHolder.tvBoutiqueName.setText(boutiqueBeen.getName());
        boutiqueViewHolder.tvBoutiqueDes.setText(boutiqueBeen.getDescription());
        //下载精品图片并设置
        ImageLoader.downloadImg(context,boutiqueViewHolder.ivBoutique,boutiqueBeen.getImageurl());
        //向单击事件监听传递该商品对象
        boutiqueViewHolder.layoutBoutique.setTag(boutiqueBeen);
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList != null ? mBoutiqueList.size() : 0;
    }


    public void initBoutiqueList(ArrayList<BoutiqueBean> boutiqueList) {
        if(mBoutiqueList!=null){
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(boutiqueList);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutique)
        ImageView ivBoutique;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDes)
        TextView tvBoutiqueDes;
        @BindView(R.id.layout_boutique)
        LinearLayout layoutBoutique;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_boutique)
        public void onItemClick(){
            BoutiqueBean boutiqueBean= (BoutiqueBean) layoutBoutique.getTag();
            L.e(boutiqueBean.toString());
            int catId=boutiqueBean.getId();
            String name=boutiqueBean.getName();
            context.startActivity(new Intent(context,Boutique2Activity.class)
                    .putExtra("catId",catId)
                    .putExtra("name",name));
        }
    }
}
