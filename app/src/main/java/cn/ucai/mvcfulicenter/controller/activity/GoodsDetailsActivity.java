package cn.ucai.mvcfulicenter.controller.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.AlbumsBean;
import cn.ucai.mvcfulicenter.bean.GoodsDetailsBean;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.net.IModelAddCart;
import cn.ucai.mvcfulicenter.model.net.IModelAddCollect;
import cn.ucai.mvcfulicenter.model.net.IModelDeleteCollect;
import cn.ucai.mvcfulicenter.model.net.IModelGoodsDetails;
import cn.ucai.mvcfulicenter.model.net.IModelIsCollect;
import cn.ucai.mvcfulicenter.model.net.ModelAddCart;
import cn.ucai.mvcfulicenter.model.net.ModelAddCollect;
import cn.ucai.mvcfulicenter.model.net.ModelDeleteCollect;
import cn.ucai.mvcfulicenter.model.net.ModelGoodsDetails;
import cn.ucai.mvcfulicenter.model.net.ModelIsCollect;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;
import cn.ucai.mvcfulicenter.model.utils.OkHttpUtils;
import cn.ucai.mvcfulicenter.view.FlowIndicator;
import cn.ucai.mvcfulicenter.view.SlideAutoLoopView;


public class GoodsDetailsActivity extends AppCompatActivity {
    private final String TAG = GoodsDetailsActivity.class.getSimpleName();
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.ivCart)
    ImageView ivCart;
    @BindView(R.id.tvGoodEnglishName)
    TextView tvGoodEnglishName;
    @BindView(R.id.tvGoodName)
    TextView tvGoodName;
    @BindView(R.id.tvGoodShopPrice)
    TextView tvGoodShopPrice;
    @BindView(R.id.tvGoodCurrentPrice)
    TextView tvGoodCurrentPrice;
    @BindView(R.id.slideAuto)
    SlideAutoLoopView slideAuto;
    @BindView(R.id.flowIndicator)
    FlowIndicator flowIndicator;
    @BindView(R.id.wvGoodBrief)
    WebView wvGoodBrief;
    int goodsId;
    Context context;
    IModelGoodsDetails modelGoodsDetails;
    IModelDeleteCollect modelDeleteCollect;
    IModelAddCollect modelAddCollect;
    IModelAddCart modelAddCart;
    IModelIsCollect modelIsCollect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        modelAddCollect =new ModelAddCollect();
        modelDeleteCollect = new ModelDeleteCollect();
        modelGoodsDetails = new ModelGoodsDetails();
        modelAddCart = new ModelAddCart();
        modelIsCollect = new ModelIsCollect();
        context = GoodsDetailsActivity.this;
        initView();
        initData();
    }

    private void initView() {

    }

    GoodsDetailsBean mGoodsDetail;

    private void initData() {
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        if (goodsId > 0) {
            modelGoodsDetails.downloadGoodsDetails(GoodsDetailsActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
                @Override
                public void onSuccess(GoodsDetailsBean result) {
                    if (result != null) {
                        mGoodsDetail = result;
                        //在相应控件上显示商品详情
                        showGoodsDetails();
                    }
                }

                @Override
                public void onError(String error) {
                    finish();
                    Toast.makeText(GoodsDetailsActivity.this, "下载商品详情失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            finish();
            Toast.makeText(GoodsDetailsActivity.this, "下载商品详情失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showGoodsDetails() {
        tvGoodEnglishName.setText(mGoodsDetail.getGoodsEnglishName());
        tvGoodName.setText(mGoodsDetail.getGoodsName());
        tvGoodShopPrice.setText(/*mGoodsDetail.getShopPrice()*/"");
        tvGoodCurrentPrice.setText(mGoodsDetail.getCurrencyPrice());
        //设置滚动循环播放商品图片
        slideAuto.startPlayLoop(flowIndicator, getAlbumImgUrl(), getAlbumSize());
        wvGoodBrief.loadDataWithBaseURL(null, mGoodsDetail.getGoodsBrief(), I.TEXT_HTML, "utf-8", null);
    }

    //得到商品图片个数
    private int getAlbumSize() {
        if (mGoodsDetail.getProperties() != null && mGoodsDetail.getProperties().size() > 0) {
            return mGoodsDetail.getProperties().get(0).getAlbums().size();
        }
        return 0;
    }

    //得到商品图片集
    private String[] getAlbumImgUrl() {
        String[] albumImgUrls = null;
        if (mGoodsDetail.getProperties() != null && mGoodsDetail.getProperties().size() > 0) {
            List<AlbumsBean> albumsBeanList = mGoodsDetail.getProperties().get(0).getAlbums();
            albumImgUrls = new String[albumsBeanList.size()];
            for (int i = 0; i < albumsBeanList.size(); i++) {
                albumImgUrls[i] = albumsBeanList.get(i).getImgUrl();
            }
        }
        return albumImgUrls;
    }

    @OnClick(R.id.ivBack)
    public void oBackClick() {
        MFGT.finish(this);
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    //判断该商品是否被收藏
    boolean isCollected = false;

    private void isCollected() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if (user != null) {
            modelIsCollect.isCollect(context, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result.isSuccess() && result != null) {
                        isCollected = true;
                    } else {
                        isCollected = false;
                    }
                    updateGoodCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodCollectStatus();
                }
            });
        } else {
            updateGoodCollectStatus();
        }
    }

    //
    private void updateGoodCollectStatus() {
        if (isCollected) {
            ivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }

    @OnClick({R.id.ivShare, R.id.ivCollect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivShare:
                showShare();
                break;
            case R.id.ivCollect:
                final UserAvatar user = FuLiCenterApplication.getUser();
                if (user == null) {
                    MFGT.gotoLoginActivity((Activity) context);
                } else {
                    if (isCollected) {
                        modelDeleteCollect.deleteCollect(context, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                CommonUtils.showShortToast(result.getMsg());
                                isCollected = !isCollected;
                                updateGoodCollectStatus();
                            }

                            @Override
                            public void onError(String error) {
                                L.e(TAG+" error="+error);
                            }
                        });
                    } else {
                        modelAddCollect.addCollectGood(context, goodsId, user.getMuserName(), new OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                CommonUtils.showShortToast(result.getMsg());
                                isCollected = !isCollected;
                                updateGoodCollectStatus();
                            }

                            @Override
                            public void onError(String error) {
                                L.e(TAG+" error="+error);
                            }
                        });
                    }
                }
                break;
        }
    }



    @OnClick(R.id.ivCart)
    public void addCartOnClick() {
        UserAvatar user = FuLiCenterApplication.getUser();
        if(user==null){
            MFGT.gotoLoginActivity((Activity) context);
        }else{
            modelAddCart.addCart(context, goodsId, user.getMuserName(), 1, false, new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    CommonUtils.showShortToast(getResources().getString(R.string.add_cart_success));
                }

                @Override
                public void onError(String error) {
                    L.e(TAG+" error="+error);
                }
            });
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
