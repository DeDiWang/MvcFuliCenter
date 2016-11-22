package cn.ucai.mvcfulicenter.controller.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.controller.activity.CollectGoodsActivity;
import cn.ucai.mvcfulicenter.controller.activity.SettingActivity;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.net.IModelCollectCount;
import cn.ucai.mvcfulicenter.model.net.ModelCollectCount;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMe extends Fragment {
    private static final String TAG = FragmentMe.class.getSimpleName();
    @BindView(R.id.ivUserThumb)
    ImageView ivUserThumb;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.goodsCount)
    TextView goodsCount;
    @BindView(R.id.shopsCount)
    TextView shopsCount;
    @BindView(R.id.footCount)
    TextView footCount;
    @BindView(R.id.tvSetting)
    TextView tvSetting;

    public FragmentMe() {
        // Required empty public constructor
    }

    Context mContext;
    UserAvatar user;
    IModelCollectCount modelCollectCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        modelCollectCount = new ModelCollectCount();
        mContext = getActivity();
        initData();
        return view;
    }

    private void initData() {
        UserAvatar user = FuLiCenterApplication.getUser();
        L.e(TAG + " user==" + user);
        if (user != null) {
            tvNick.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserThumb);
        } else {
            MFGT.gotoLoginActivity((Activity) mContext);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            tvNick.setText(user.getMuserNick());
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserThumb);
            //下载收藏商品数量并更新数据
            findCollectCount();
        }
    }

    @OnClick({R.id.tvSetting, R.id.ivUserThumb, R.id.tvNick, R.id.ivBarcode})
    public void onClick() {
        startActivity(new Intent(mContext, SettingActivity.class));
    }

    private void findCollectCount() {
        modelCollectCount.findCollectCount(mContext, user.getMuserName(), new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    goodsCount.setText(result.getMsg());
                } else {
                    goodsCount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                goodsCount.setText(String.valueOf(0));
                CommonUtils.showShortToast(error);
            }
        });
    }

    @OnClick(R.id.collectGoods)
    public void onClickCollectGoods() {
        startActivity(new Intent(mContext, CollectGoodsActivity.class));
    }
}
