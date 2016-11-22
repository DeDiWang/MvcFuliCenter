package cn.ucai.mvcfulicenter.controller.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.mvcfulicenter.I;
import cn.ucai.mvcfulicenter.R;
import cn.ucai.mvcfulicenter.controller.fragment.FragmentBoutique;
import cn.ucai.mvcfulicenter.controller.fragment.FragmentCart;
import cn.ucai.mvcfulicenter.controller.fragment.FragmentCategory;
import cn.ucai.mvcfulicenter.controller.fragment.FragmentMe;
import cn.ucai.mvcfulicenter.controller.fragment.FragmentNewGoods;
import cn.ucai.mvcfulicenter.model.FuLiCenterApplication;
import cn.ucai.mvcfulicenter.model.utils.L;

public class MainActivity extends AppCompatActivity {

    FragmentNewGoods mFragmentNewGoods;
    FragmentBoutique mFragmentBoutique;
    FragmentCategory mFragmentCategory;
    FragmentCart mFragmentCart;
    FragmentMe mFragmentMe;
    List<Fragment> mFragmentList;
    int index = 0;//默认fragment的下标
    @BindView(R.id.rbNewGoods)
    RadioButton mrbNewGoods;
    @BindView(R.id.rbBoutique)
    RadioButton mrbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton mrbCategory;
    @BindView(R.id.rbCart)
    RadioButton mrbCart;
    @BindView(R.id.rbPersonalCenter)
    RadioButton mrbMe;
    RadioButton[] rbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();


    }

    private void initRadioButtonStatus() {
        rbs=new RadioButton[5];
        rbs[0]=mrbNewGoods;
        rbs[1]=mrbBoutique;
        rbs[2]=mrbCategory;
        rbs[3]=mrbCart;
        rbs[4]=mrbMe;
        for(int i=0;i<rbs.length;i++){
            if(i==index){
                rbs[i].setChecked(true);
            }else{
                rbs[i].setChecked(false);
            }
        }
    }

    private void initView() {
        mFragmentNewGoods = new FragmentNewGoods();
        mFragmentBoutique = new FragmentBoutique();
        mFragmentCategory = new FragmentCategory();
        mFragmentCart = new FragmentCart();
        mFragmentMe = new FragmentMe();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mFragmentNewGoods);
        mFragmentList.add(mFragmentBoutique);
        mFragmentList.add(mFragmentCategory);
        mFragmentList.add(mFragmentCart);
        mFragmentList.add(mFragmentMe);
        //设置默认显示的fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, mFragmentNewGoods).show(mFragmentNewGoods).commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbCart:
                if(FuLiCenterApplication.getUser()==null){
                    startActivityForResult(new Intent(this,LoginActivity.class),I.REQUEST_CODE_LOGIN_FROM_CART);
                }else{
                    index = 3;
                }
                break;
            case R.id.rbPersonalCenter:
                if(FuLiCenterApplication.getUser()==null){
                    startActivityForResult(new Intent(this,LoginActivity.class),I.REQUEST_CODE_LOGIN);
                }else{
                    index = 4;
                }
                break;
            default:
                index=0;
                break;
        }
        setFragment();
    }

    private void setFragment() {
        initRadioButtonStatus();
        switchFragment(index);
    }

    //跳转Fragment
    int currentIndex;//记录当前的Fragment下标
    private void switchFragment(int index) {
        if (index == currentIndex) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = mFragmentList.get(index);
        if (!fragment.isAdded()) {
            ft.add(R.id.fragmentContainer, fragment);
        }
        ft.show(fragment).hide(mFragmentList.get(currentIndex)).commitAllowingStateLoss();
        currentIndex = index;
    }
    //MyBroadcastReceiver myBroadcastReciver;
    @Override
    protected void onResume() {
        super.onResume();
        if(FuLiCenterApplication.getUser()==null && index==4){
            index =0;
        }
        setFragment();
        /*setFragment();
        myBroadcastReciver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("gotoNewGoodsActivity");
        registerReceiver(myBroadcastReciver, filter);*/
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReciver);
    }*/

    //先于onResume方法执行
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("MainActivity.onActivityResult requestCode=="+requestCode);
        //判断是否是从登录页面跳转回来的
        if(FuLiCenterApplication.getUser()!=null){
            if(requestCode == I.REQUEST_CODE_LOGIN ){
                index=4;
            }
            if(requestCode ==I.REQUEST_CODE_LOGIN_FROM_CART){
                index=3;
            }
        }
    }
}
