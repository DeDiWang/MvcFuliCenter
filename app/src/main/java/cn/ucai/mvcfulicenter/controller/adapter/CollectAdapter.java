package cn.ucai.mvcfulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.CollectBean;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.net.IModelDeleteCollect;
import cn.ucai.mvcfulicenter.model.net.ModelDeleteCollect;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;


/**
 * Created by 11039 on 2016/10/26.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CollectBean> list;
    IModelDeleteCollect model;
    public CollectAdapter(Context context, ArrayList<CollectBean> list) {
        this.context = context;
        this.list = list;
        model = new ModelDeleteCollect();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == I.TYPE_FOOTER) {
            view = View.inflate(context, R.layout.item_footer, null);
            holder = new FooterViewHolder(view);
        } else {
            view = View.inflate(context, R.layout.item_collect_good, null);
            holder = new CollectViewHolder(view);
        }
        return holder;
    }

    private String footer;

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    private boolean isMore;

    public void setMore(boolean isMore) {
        this.isMore = isMore;
    }

    public boolean isMore() {
        return isMore;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(footer);
            return;
        }
        CollectBean collectBean = list.get(position);
        CollectViewHolder collectViewHolder = (CollectViewHolder) holder;
        ImageLoader.downloadImg(context, collectViewHolder.ivGoodAvatar, collectBean.getGoodsThumb());
        collectViewHolder.tvGoodName.setText(collectBean.getGoodsName());
        //将当前收藏商品项对象传给监听事件
        collectViewHolder.layoutCollectGood.setTag(collectBean);
    }

    public void initData(ArrayList<CollectBean> collectList) {
        this.list.clear();
        this.list.addAll(collectList);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> collectList) {
        this.list.addAll(collectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivDelete)
        ImageView ivDelete;
        @BindView(R.id.ivGoodAvatar)
        ImageView ivGoodAvatar;
        @BindView(R.id.tvGoodName)
        TextView tvGoodName;
        @BindView(R.id.layout_collect_good)
        RelativeLayout layoutCollectGood;
        CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.layout_collect_good)
        public void onCollectGoodClick() {
            CollectBean collectBean = (CollectBean) layoutCollectGood.getTag();
            //跳转到商品详情页
            MFGT.gotoDetailsActivity(context,collectBean.getGoodsId());
        }

        //删除当前收藏商品项
        @OnClick(R.id.ivDelete)
        public void onDeleteClick() {
            final CollectBean good = (CollectBean) layoutCollectGood.getTag();
            String userName = FuLiCenterApplication.getUser().getMuserName();
            model.deleteCollect(context, good.getGoodsId(), userName, new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result.isSuccess() && result!=null){
                        //从当前收藏列表中删除该商品
                        list.remove(good);
                        notifyDataSetChanged();
                    }else{
                        CommonUtils.showShortToast(result!=null?result.getMsg():
                                context.getResources().getString(R.string.delete_collect_fail));
                    }
                }

                @Override
                public void onError(String error) {
                    L.e("error==="+error);
                    CommonUtils.showShortToast(context.getResources().getString(R.string.delete_collect_fail));
                }
            });
        }
    }
}
