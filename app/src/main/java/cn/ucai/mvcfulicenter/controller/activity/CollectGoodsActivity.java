package cn.ucai.mvcfulicenter.controller.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.I;

import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.CollectBean;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.controller.adapter.CollectAdapter;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.net.IModelCollectGoods;
import cn.ucai.mvcfulicenter.model.net.ModelCollectGoods;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.ConvertUtils;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;


public class CollectGoodsActivity extends AppCompatActivity {
    private final String TAG = CollectGoodsActivity.class.getSimpleName();
    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvCollectGoods)
    RecyclerView mrvCollectGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    Context mContext;
    ArrayList<CollectBean> mCollectList;
    CollectAdapter mAdapter;
    GridLayoutManager gridManager;

    IModelCollectGoods model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_goods);
        ButterKnife.bind(this);
        model = new ModelCollectGoods();
        mContext=this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPUllUpListener();
    }
    int lastPosition;
    //上拉加载
    private void setPUllUpListener() {
        mrvCollectGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = gridManager.findLastVisibleItemPosition();
                //mAdapter.setScrollState(newState);
                //L.e("lastPosition="+lastPosition+",count="+mAdapter.getItemCount()+",isMore="+mAdapter.isMore());
                if(newState== RecyclerView.SCROLL_STATE_IDLE &&
                        lastPosition>=mAdapter.getItemCount()-1 &&
                        mAdapter.isMore()){
                    mPageId++;
                    downloadCollectList(mPageId,I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    //下拉刷新
    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                mSrl.setEnabled(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId=1;
                downloadCollectList(mPageId,I.ACTION_PULL_DOWN);
            }
        });
    }

    int mPageId=1;
    private void initData() {
        downloadCollectList(I.ACTION_DOWNLOAD,mPageId);
    }

    private void downloadCollectList(final int action, int pageId) {
        UserAvatar user = FuLiCenterApplication.getUser();
        model.downloadCollectGoods(mContext, user.getMuserName(), pageId,
                new OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mSrl.setRefreshing(false);
                mtvRefreshHint.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if(result!=null){
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD || action==I.ACTION_PULL_DOWN){
                        mAdapter.initData(list);
                    }else{
                        mAdapter.addData(list);
                    }
                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                        mAdapter.setFooter("无更多可加载");
                    }
                }else {
                    mAdapter.setMore(false);
                    mAdapter.setFooter("无更多可加载");
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG+"error==",error);
            }
        });
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.green)
        );

        mCollectList=new ArrayList<>();
        mAdapter=new CollectAdapter(mContext,mCollectList);
        mrvCollectGoods.setAdapter(mAdapter);
        gridManager=new GridLayoutManager(mContext,2);
        mrvCollectGoods.setLayoutManager(gridManager);
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
