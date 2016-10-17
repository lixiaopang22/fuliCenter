package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.fragment.NewGoodsFragment;
import com.example.administrator.fulicenter.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
    RadioButton [] rbs;
    Fragment [] mFragment;
    NewGoodsFragment mNewGoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        L.i("main", "MainActivity onCreate()");
        initView();
        initFragment();
    }

    private void initFragment() {
        mFragment=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
                .show(mNewGoodsFragment)
                .commit();
    }

    private void initView() {
        rbs=new RadioButton[5];
        rbs [0]=newGoods;
        rbs [1]=Boutique;
        rbs [2]=Category;
        rbs [3]=Cart;
        rbs [4]=Center;
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
        setRadioButtonStatus();
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
