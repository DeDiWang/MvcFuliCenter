package cn.ucai.mvcfulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.bean.MessageBean;
import cn.ucai.mvcfulicenter.model.net.IModelDeleteCart;
import cn.ucai.mvcfulicenter.model.net.ModelDeleteCart;
import cn.ucai.mvcfulicenter.model.net.OnCompleteListener;

import cn.ucai.mvcfulicenter.model.utils.L;
import cn.ucai.mvcfulicenter.model.utils.MFGT;


public class AddressActivity extends AppCompatActivity {
    private final String TAG = AddressActivity.class.getSimpleName();
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.etCity)
    Spinner etCity;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tvSum)
    TextView tvSum;

    String[] arrCartId;
    double orderPrice;
    private static String URL = "http://218.244.151.190/demo/charge";
    IModelDeleteCart model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        model = new ModelDeleteCart();
        String cartIds = getIntent().getStringExtra("cartIds");
        arrCartId = cartIds.substring(0,cartIds.length()).split(",");
        L.e("arrCartId============"+Arrays.toString(arrCartId));
        orderPrice = getIntent().getDoubleExtra("orderPrice", 0);
        initView();

    }

    private void initView() {
        tvSum.setText("￥"+String.valueOf(orderPrice));
    }

    @OnClick({R.id.ivBack, R.id.btnBuy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(this);
                break;
            case R.id.btnBuy:
                String userName = etName.getText().toString();
                if(TextUtils.isEmpty(userName)){
                    etName.setError("用户名不能为空");
                    etName.requestFocus();
                    return;
                }
                String number = etNumber.getText().toString();
                if(TextUtils.isEmpty(number)){
                    etNumber.setText("手机号不能为空");
                    etNumber.requestFocus();
                    return;
                }
                if(!number.matches("[\\d]{11}")){
                    etNumber.setError("手机号格式不正确");
                    etNumber.requestFocus();
                    return;
                }
                String address = etAddress.getText().toString();
                if(TextUtils.isEmpty(address)){
                    etAddress.setError("地址不能为空");
                    etAddress.requestFocus();
                    return;
                }
                gotoBuy();
                break;
        }
    }

    private void gotoBuy() {
        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", orderPrice*100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void paySuccess() {
        for(String id:arrCartId){
            model.deleteCartGood(this, Integer.parseInt(id), new OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    L.e("result====="+result);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}
