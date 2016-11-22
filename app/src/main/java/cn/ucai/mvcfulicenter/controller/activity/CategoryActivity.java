package cn.ucai.mvcfulicenter.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.I;

import cn.ucai.mvcfulicenter.bean.CategoryChildBean;
import cn.ucai.mvcfulicenter.bean.NewGoodsBean;
import cn.ucai.mvcfulicenter.controller.adapter.GoodAdapter;
import cn.ucai.mvcfulicenter.model.net.IModelCategory2Child;
import cn.ucai.mvcfulicenter.model.net.ModelCategory2Child;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.ConvertUtils;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;

import cn.ucai.mvcfulicenter.view.CatChildFilterButton;


public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mivBack;
    @BindView(R.id.btnSortPrice)
    Button mbtnSortPrice;
    @BindView(R.id.btnSortTime)
    Button mbtnSortTime;
    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvCategoryChild)
    RecyclerView mrvCategoryChild;
    @BindView(R.id.srlCategoryChild)
    SwipeRefreshLayout msrlCategoryChild;
    GoodAdapter mAdapter;
    Context mContext;
    ArrayList<NewGoodsBean> goodsList;
    GridLayoutManager gridManager;

    int catId;
    String groupName;
    ArrayList<CategoryChildBean> list;
    @BindView(R.id.btnCatChildFilter)
    CatChildFilterButton mbtnCatChildFilter;

    IModelCategory2Child model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        model = new ModelCategory2Child();
        //拿到分类首页Fragment中传来的数据
        catId = getIntent().getIntExtra("childId", 0);
        L.e("??????????????"+catId);
        groupName = getIntent().getStringExtra("groupName");
        list = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("list");
        mContext = this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPUllUpListener();
        mivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.finish((Activity) mContext);
            }
        });

        mbtnCatChildFilter.setOnCatFilterClickListener(groupName, list);
    }

    boolean priceAsc = false;
    boolean timeAsc = false;
    int sortBy = I.SORT_BY_PRICE_ASC;

    @OnClick({R.id.btnSortPrice, R.id.btnSortTime})
    public void onClick(View view) {
        Drawable right;
        switch (view.getId()) {
            case R.id.btnSortPrice:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mbtnSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                priceAsc = !priceAsc;
                break;
            case R.id.btnSortTime:
                if (timeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                mbtnSortTime.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                timeAsc = !timeAsc;
                break;
        }
        mAdapter.setSortBy(sortBy);
    }

    int lastPosition;

    //上拉加载
    private void setPUllUpListener() {
        mrvCategoryChild.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = gridManager.findLastVisibleItemPosition();
                mAdapter.setScrollState(newState);
                L.e("lastPosition=" + lastPosition + ",count=" + mAdapter.getItemCount() + ",isMore=" + mAdapter.isMore());
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastPosition >= mAdapter.getItemCount() - 1 &&
                        mAdapter.isMore()) {
                    mPageId++;
                    downloadCategoryChild(mPageId, I.ACTION_PULL_UP);
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
        msrlCategoryChild.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrlCategoryChild.setRefreshing(true);
                msrlCategoryChild.setEnabled(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadCategoryChild(mPageId, I.ACTION_PULL_DOWN);
            }
        });
    }

    int mPageId = 1;

    private void initData() {
        downloadCategoryChild(mPageId, I.ACTION_DOWNLOAD);
    }

    private void downloadCategoryChild(int pageId, final int action) {
        model.downloadCategory2Child(mContext, catId, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                msrlCategoryChild.setRefreshing(false);
                mtvRefreshHint.setVisibility(View.GONE);
                mAdapter.setFooter("加载更多...");
                mAdapter.setMore(true);
                if (result != null) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_PULL_DOWN || action == I.ACTION_DOWNLOAD) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                        mAdapter.setFooter("不能加载更多...");
                    }
                } else {
                    mAdapter.setMore(false);
                    mAdapter.setFooter("不能加载更多...");
                }
            }

            @Override
            public void onError(String error) {
                L.e("error" + error);
            }
        });
    }

    private void initView() {
        mbtnCatChildFilter.setText(groupName);
        goodsList = new ArrayList<>();
        mAdapter = new GoodAdapter(mContext, goodsList);
        mrvCategoryChild.setAdapter(mAdapter);
        //下拉刷新圈圈的颜色
        msrlCategoryChild.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        gridManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        gridManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrvCategoryChild.setLayoutManager(gridManager);

    }
}
