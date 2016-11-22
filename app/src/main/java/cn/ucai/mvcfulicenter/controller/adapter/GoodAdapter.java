package cn.ucai.mvcfulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.NewGoodsBean;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.MFGT;


/**
 * Created by 11039 on 2016/10/17.
 */
public class GoodAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<NewGoodsBean> goodsList;

    //添加排序功能
    int sortBy= I.SORT_BY_PRICE_ASC;
    public void setSortBy(int sortBy){
        this.sortBy=sortBy;
        sortBy();
        notifyDataSetChanged();
    }

    private void sortBy() {
        Collections.sort(goodsList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean left, NewGoodsBean right) {
                int result=0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result= (int) (Long.valueOf(left.getAddTime())-Long.valueOf(right.getAddTime()));
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result= (int) (Long.valueOf(right.getAddTime())-Long.valueOf(left.getAddTime()));
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result= getPrice(left.getCurrencyPrice())-getPrice(right.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result= getPrice(right.getCurrencyPrice())-getPrice(left.getCurrencyPrice());
                        break;
                }
                return result;
            }
            private int getPrice(String currencyPrice) {
                return Integer.parseInt(currencyPrice.substring(currencyPrice.indexOf("￥")+1));
            }
        });
    }

    //设置商品列表项单击事件的监听对象
    View.OnClickListener mItemOnClickListener;
    public GoodAdapter(final Context context, ArrayList<NewGoodsBean> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        //只需设置一个监听对象
        mItemOnClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拿到该项的商品id
                int goodsId = (int) view.getTag();
                //跳转到商品详情的Activity
                /*context.startActivity(new Intent(context, GoodsDetailsActivity.class)
                .putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId));*/
                MFGT.gotoDetailsActivity(context,goodsId);
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer, null));
        } else {
            holder = new GoodViewHolder(inflater.inflate(R.layout.item_goods, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(footer);
            return;
        }
        GoodViewHolder goodViewHolder = (GoodViewHolder) holder;
        NewGoodsBean good = goodsList.get(position);
        //下载商品图片
        ImageLoader.downloadImg(context, goodViewHolder.ivGoodAvatar, good.getGoodsThumb());
        goodViewHolder.tvGoodName.setText(good.getGoodsName());
        goodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());

        //给单击事件监听对象传递数据
        goodViewHolder.layoutGoods.setTag(good.getGoodsId());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return goodsList != null ? goodsList.size() + 1 : 1;
    }

    public void initData(ArrayList<NewGoodsBean> mGoodsList) {
        if (goodsList != null) {
            goodsList.clear();
        }
        goodsList.addAll(mGoodsList);
        sortBy();
        notifyDataSetChanged();
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

    public void addData(ArrayList<NewGoodsBean> list) {
        this.goodsList.addAll(list);
        sortBy();
        notifyDataSetChanged();
    }

    int newState;

    public void setScrollState(int newState) {
        this.newState = newState;
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodAvatar)
        ImageView ivGoodAvatar;
        @BindView(R.id.tvGoodName)
        TextView tvGoodName;
        @BindView(R.id.tvGoodPrice)
        TextView tvGoodPrice;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;
        GoodViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //设置列表项的监听
            layoutGoods.setOnClickListener(mItemOnClickListener);
        }
        /*@OnClick(R.id.layout_goods)
        public void onItemOnClick(){
            int goodsId= (int) layoutGoods.getTag();
            MFGT.gotoDetailsActivity(context,goodsId);
        }*/
    }

}
