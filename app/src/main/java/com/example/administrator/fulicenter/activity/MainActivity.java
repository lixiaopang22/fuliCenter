package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.fragment.BoutiqueFragment;
import com.example.administrator.fulicenter.fragment.NewGoodsFragment;
import com.example.administrator.fulicenter.utils.L;

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
        mFragment[0]=mNewGoodsFragment;
        mFragment[1]=mBoutiqueFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .add(R.id.fragment_container,mBoutiqueFragment)
                .hide(mBoutiqueFragment)
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
                index=3;
                break;
            case R.id.Center :
                index=4;
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
}
