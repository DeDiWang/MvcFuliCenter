package cn.ucai.mvcfulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.CartBean;
import cn.ucai.mvcfulicenter.bean.GoodsDetailsBean;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.controller.activity.AddressActivity;
import cn.ucai.mvcfulicenter.controller.adapter.CartAdapter;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.net.IModelFindCarts;
import cn.ucai.mvcfulicenter.model.net.ModelFindCarts;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.ConvertUtils;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;
import cn.ucai.mvcfulicenter.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCart extends Fragment {

    @BindView(R.id.tvRefreshHint)
    TextView tvRefreshHint;
    @BindView(R.id.rvCartGoods)
    RecyclerView rvCartGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tvSum)
    TextView tvSum;
    @BindView(R.id.tvSave)
    TextView tvSave;

    Context mContext;
    ArrayList<CartBean> cartList;
    CartAdapter mAdapter;
    LinearLayoutManager layoutManager;
    @BindView(R.id.tvNoting)
    TextView tvNoting;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    IModelFindCarts modelFindCarts;
    public FragmentCart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        modelFindCarts = new ModelFindCarts();
        mContext = getContext();
        initView();
        initData();
        setListener();
        return view;
    }

    MyBroadcast myBroadcast;

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefreshHint.setVisibility(View.VISIBLE);
                initData();
            }
        });
        //注册广播接收者
        myBroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        mContext.registerReceiver(myBroadcast, filter);
    }

    @OnClick(R.id.btnBuy)
    public void onClick() {
        if(orderPrice>0 && cartIds.length()>0 && !cartIds.equals("")){
            startActivity(new Intent(mContext, AddressActivity.class)
                    .putExtra("cartIds",cartIds).putExtra("orderPrice",orderPrice));
        }else{
            CommonUtils.showShortToast(getResources().getString(R.string.noting_to_buy));
        }
    }

    //定义广播者类
    class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
        }
    }

    private void initData() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if (user != null) {
            modelFindCarts.findCarts(mContext, user.getMuserName(), new OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    srl.setRefreshing(false);
                    tvRefreshHint.setVisibility(View.GONE);
                    ArrayList<CartBean> list = ConvertUtils.array2List(result);
                    if (list != null && list.size() > 0) {
                        mAdapter.initData(list);
                        setCartLayout(true);
                    } else {
                        setCartLayout(false);
                    }
                    sumPrice();
                }

                @Override
                public void onError(String error) {
                    L.e("error====" + error);
                }
            });
        }
    }

    public void setCartLayout(boolean hasCart) {
        if (hasCart) {
            tvNoting.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
        } else {
            tvNoting.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.GONE);
        }
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.google_yellow)
        );
        cartList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, cartList);
        rvCartGoods.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(mContext);
        rvCartGoods.setLayoutManager(layoutManager);
        rvCartGoods.setHasFixedSize(true);
        rvCartGoods.addItemDecoration(new SpaceItemDecoration(20));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    double orderPrice;
    String cartIds = "";

    //计算购物车商品总价
    private void sumPrice() {
        double sum = 0;
        double rank = 0;
        if (cartList != null && cartList.size() > 0) {
            for (CartBean c : cartList) {
                GoodsDetailsBean goods = c.getGoods();
                if (c.isChecked()) {
                    sum += c.getCount() * getPrice(goods.getCurrencyPrice());
                    rank += c.getCount() * getPrice(goods.getRankPrice());
                    cartIds += c.getId() + ",";
                }
            }
            orderPrice = rank;
            tvSum.setText("总计：￥" + rank);
            tvSave.setText("节省：￥" + (sum - rank));
        } else {
            orderPrice = 0;
            setCartLayout(false);
            tvSum.setText("总计：￥0");
            tvSave.setText("节省：￥0");
        }
    }

    private int getPrice(String str) {
        String s = str.substring(str.indexOf("￥") + 1);
        int i = Integer.parseInt(s);
        return i;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(myBroadcast);
    }
}
