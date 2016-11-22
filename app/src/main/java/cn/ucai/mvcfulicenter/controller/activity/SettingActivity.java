package cn.ucai.mvcfulicenter.controller.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.Result;
import cn.ucai.mvcfulicenter.bean.UserAvatar;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.SharedPreferencesUtils;
import cn.ucai.mvcfulicenter.model.db.UserDao;
import cn.ucai.mvcfulicenter.model.net.IModelUpdateAvatar;
import cn.ucai.mvcfulicenter.model.net.IModelUpdateNick;
import cn.ucai.mvcfulicenter.model.net.ModelUpdateAvatar;
import cn.ucai.mvcfulicenter.model.net.ModelUpdateNick;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.ImageLoader;
import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;
import cn.ucai.mvcfulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.mvcfulicenter.model.utils.ResultUtils;


public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.ivThumb)
    ImageView ivThumb;
    @BindView(R.id.tvNick)
    TextView tvNick;

    Activity mContext;
    IModelUpdateNick modelUpdateNick;
    IModelUpdateAvatar modelUpdateAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    UserAvatar user;
    OnSetAvatarListener mOnSetAvatarListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        modelUpdateAvatar = new ModelUpdateAvatar();
        modelUpdateNick = new ModelUpdateNick();
        mContext = this;
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivThumb);
            tvNick.setText(user.getMuserNick());
            tvUserName.setText(user.getMuserName());
        } else {
            finish();
        }
    }

    @OnClick({R.id.ivBack, R.id.setThumb, R.id.setUserName, R.id.setNick, R.id.btnExist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.setThumb:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_settings,
                        user.getMuserName(), user.getMavatarPath());
                break;
            case R.id.setUserName:
                CommonUtils.showLongToast(getResources().getString(R.string.user_name_cannot_be_updated));
                break;
            case R.id.setNick:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.et_update_nick, null);
                final EditText etUpdateNick= (EditText) view1.findViewById(R.id.etUpdateNick);
                etUpdateNick.setText(user.getMuserNick());
                //弹出修改昵称的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.setTitle("修改昵称")
                        .setView(view1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newNick = etUpdateNick.getText().toString();
                                checkNick(newNick);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.btnExist:
                if (FuLiCenterApplication.getUser() != null) {
                    //退出登录，删除首选项中的用户名和内存中的用户信息
                    SharedPreferencesUtils.getInstance(mContext).removeUser();
                    FuLiCenterApplication.setUser(null);
                    //然后跳转到登录界面
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                MFGT.finish(mContext);
                break;
        }
    }

    private void checkNick(String newNick) {
        if(TextUtils.isEmpty(newNick)){
            CommonUtils.showShortToast("昵称不能为空");
        }else if(newNick.equals(user.getMuserNick())){
            CommonUtils.showShortToast("昵称未修改");
        }else{
            updateNick(newNick);
        }
    }

    private void updateNick(String newNick) {
        modelUpdateNick.updateNick(mContext, user.getMuserName(), newNick, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, UserAvatar.class);
                if(result==null){
                    CommonUtils.showShortToast(getResources().getString(R.string.updateNick_fail));
                }else{
                    if(result.isRetMsg()){
                        UserAvatar u = (UserAvatar) result.getRetData();
                        L.e("u====="+u);
                        //将新用户信息保存在数据库中
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.updateUser(u);
                        if(isSuccess){
                            //保存在内存中
                            FuLiCenterApplication.setUser(u);

                            tvNick.setText(u.getMuserNick());
                        }else{
                            CommonUtils.showShortToast(getResources().getString(R.string.user_database_error));
                        }
                    }else{
                        if(result.getRetCode()== I.MSG_USER_SAME_NICK){
                            CommonUtils.showShortToast(getResources().getString(R.string.user_nick_unmodify));
                        }else if(result.getRetCode()== I.MSG_USER_UPDATE_NICK_FAIL){
                            CommonUtils.showShortToast(getResources().getString(R.string.updateNick_fail));
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(getResources().getString(R.string.updateNick_fail));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult  requestCode="+requestCode+" resultCode="+resultCode);
        mOnSetAvatarListener.setAvatar(requestCode,data,ivThumb);
        if(requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,
                user.getMavatarPath()+"/"+user.getMuserName()
                + I.AVATAR_SUFFIX_JPG));
        L.e("file=="+file.exists());
        L.e("file=="+file.getAbsolutePath());
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setMessage(getResources().getString(R.string.update_user_avatar));
        dialog.show();
        modelUpdateAvatar.updateAvatar(mContext, user.getMuserName(), file, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, UserAvatar.class);
                if(result==null){
                    CommonUtils.showShortToast(getResources().getString(R.string.update_user_avatar_fail));
                }else{
                    UserAvatar u = (UserAvatar) result.getRetData();
                    if(result.isRetMsg()) {
                        //将新用户信息保存在内存中
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, ivThumb);
                        CommonUtils.showShortToast(getResources().getString(R.string.update_user_avatar_success));
                    }else{
                        CommonUtils.showShortToast(getResources().getString(R.string.update_user_avatar_fail));
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                L.e("error=="+error);
            }
        });
    }
}
