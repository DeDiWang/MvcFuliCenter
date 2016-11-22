package cn.ucai.mvcfulicenter.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.BoutiqueBean;
import cn.ucai.mvcfulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.mvcfulicenter.model.net.IModelBoutique;
import cn.ucai.mvcfulicenter.model.net.ModelBoutique;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.ConvertUtils;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.view.SpaceItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBoutique extends Fragment {

    @BindView(R.id.tvRefreshHint)
    TextView tvRefreshHint;
    @BindView(R.id.rvBoutique)
    RecyclerView rvBoutique;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    ArrayList<BoutiqueBean> boutiqueList;
    BoutiqueAdapter mAdapter;
    LinearLayoutManager layoutManager;
    IModelBoutique modelBoutique;
    public FragmentBoutique() {

    }
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, layout);
        modelBoutique = new ModelBoutique();
        context=getContext();
        initView();
        initData();
        setListener();
        return layout;
    }

    private void setListener()
    {
        setPullDownListener();
    }
    //下拉刷新
    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setEnabled(true);
                srl.setRefreshing(true);
                tvRefreshHint.setVisibility(View.VISIBLE);
                tvRefreshHint.setText("刷新中...");
                initData();
            }
        });
    }
    private void initData() {
        downloadBoutiques();
    }

    private void downloadBoutiques() {
        modelBoutique.downloadBoutinque(context, new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                srl.setRefreshing(false);
                tvRefreshHint.setVisibility(View.GONE);
                if(result!=null){
                    L.i("result.length="+result.length);
                    ArrayList<BoutiqueBean> boutiqueList = ConvertUtils.array2List(result);
                    mAdapter.initBoutiqueList(boutiqueList);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefreshHint.setVisibility(View.GONE);
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        boutiqueList=new ArrayList<>();
        mAdapter=new BoutiqueAdapter(context,boutiqueList);
        rvBoutique.setAdapter(mAdapter);
        layoutManager=new LinearLayoutManager(context);
        rvBoutique.setLayoutManager(layoutManager);

        rvBoutique.setHasFixedSize(true);
        rvBoutique.addItemDecoration(new SpaceItemDecoration(10));
    }

}
