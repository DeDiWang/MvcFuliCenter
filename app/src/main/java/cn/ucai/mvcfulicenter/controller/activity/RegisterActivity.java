package cn.ucai.mvcfulicenter.controller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.Result;
import cn.ucai.mvcfulicenter.model.net.IModelRegister;
import cn.ucai.mvcfulicenter.model.net.ModelRegister;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;
import cn.ucai.mvcfulicenter.model.utils.CommonUtils;
import cn.ucai.mvcfulicenter.model.utils.MFGT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etNick)
    EditText etNick;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etPasswordDouble)
    EditText etPasswordDouble;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    IModelRegister model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        ButterKnife.bind(this);
        model = new ModelRegister();
    }
    String userName,nick,password,passwordDouble;
    @OnClick(R.id.btnRegister)
    public void onClick() {
        userName = etUserName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError("用户名不能为空");
            etUserName.requestFocus();
            return;
        }
        if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
            etUserName.setError("用户名以字母开头，6-16位");
            etUserName.requestFocus();
            return;
        }
        nick = etNick.getText().toString();
        if (TextUtils.isEmpty(nick)) {
            etNick.setError("昵称不能为空");
            etNick.requestFocus();
            return;
        }
        password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("密码不能为空");
            etPassword.requestFocus();
            return;
        }
        passwordDouble = etPasswordDouble.getText().toString();
        if (!password.equals(passwordDouble)) {
            etPasswordDouble.setError("两次密码不一致");
            etPasswordDouble.requestFocus();
            return;
        }

        register();

    }

    private void register() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("注册中...");
        dialog.show();
        //向服务端发注册请求
        model.register(this, userName, nick, password, new OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                dialog.dismiss();
                if (result.getRetCode() == 0) {
                    CommonUtils.showShortToast(getResources().getString(R.string.register_success));
                    //跳转回登录界面
                    setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));
                    //并关掉注册界面
                    MFGT.finish(RegisterActivity.this);
                } else if(result.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){
                    CommonUtils.showShortToast(getResources().getString(R.string.register_fail_exists));
                }
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showShortToast(error);
            }
        });
    }

    @OnClick(R.id.ivBack)
    public void onBackClick() {
        MFGT.finish(this);
    }
}
