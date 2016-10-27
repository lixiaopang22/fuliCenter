package com.example.administrator.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.fragment.BoutiqueFragment;
import com.example.administrator.fulicenter.fragment.CategoryFragment;
import com.example.administrator.fulicenter.fragment.NewGoodsFragment;
import com.example.administrator.fulicenter.fragment.PersonalCenterFragment;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.newGoods)
    RadioButton newGoods;
    @BindView(R.id.Boutique)
    RadioButton Boutique;
    @BindView(R.id.Category)
    RadioButton Category;
    @BindView(R.id.Cart)
    RadioButton Cart;
    @BindView(R.id.Center)
    RadioButton Center;

    int index;
    int currentIndex;
    RadioButton [] rbs;
    Fragment [] mFragment;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    CartFragment mCartFragment;
    PersonalCenterFragment mPersonalCenterFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("main", "MainActivity onCreate()");
        super.onCreate(savedInstanceState);
    }

    private void initFragment() {
        mFragment=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        mBoutiqueFragment=new BoutiqueFragment();
        mCategoryFragment=new CategoryFragment();
        mCartFragment=new CartFragment();
        mPersonalCenterFragment=new PersonalCenterFragment();
        mFragment[0]=mNewGoodsFragment;
        mFragment[1]=mBoutiqueFragment;
        mFragment[2]=mCategoryFragment;
        mFragment[3]=mCartFragment;
        mFragment[4]=mPersonalCenterFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .add(R.id.fragment_container,mBoutiqueFragment)
                .add(R.id.fragment_container,mCategoryFragment)
                .hide(mBoutiqueFragment)
                .hide(mCategoryFragment)
                .show(mNewGoodsFragment)
                .commit();
    }
    @Override
    protected void initView() {
        rbs=new RadioButton[5];
        rbs [0]=newGoods;
        rbs [1]=Boutique;
        rbs [2]=Category;
        rbs [3]=Cart;
        rbs [4]=Center;
    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    public void onCheckedChange(View view) {
        switch(view.getId()){
            case R.id.newGoods :
                index=0;
                break;
            case R.id.Boutique :
                index=1;
                break;
            case R.id.Category :
                index=2;
                break;
            case R.id.Cart :
                if(FuLiCenterApplication.getUser()==null){
                    MFGT.gotoCartLogin(this);
                }else {
                    index = 3;
                }
                break;
            case R.id.Center :
                if(FuLiCenterApplication.getUser()==null){
                    MFGT.gotoLogin(this);
                }else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if(index!=currentIndex){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(mFragment[currentIndex]);
            if(!mFragment[index].isAdded()){
                ft.add(R.id.fragment_container,mFragment[index]);
            }
            ft.show(mFragment[index]).commit();
        }
        setRadioButtonStatus();
        currentIndex=index;
    }

    private void setRadioButtonStatus() {
        for(int i=0;i<rbs.length;i++){
            if(i==index){
                rbs[i].setChecked(true);
            }else{
                rbs[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(index==4 && FuLiCenterApplication.getUser()==null){
            index=0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== I.REQUEST_CODE_LOGIN ){
            index=4;
        }if(requestCode==I.REQUEST_CODE_CART_LOGIN){
            index=3;
        }
    }
}
