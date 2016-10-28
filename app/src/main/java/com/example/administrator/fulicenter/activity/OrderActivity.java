package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.utils.I;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.ed_order_name)
    EditText edOrderName;
    @BindView(R.id.ed_order_phone)
    EditText edOrderPhone;
    @BindView(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @BindView(R.id.ed_order_street)
    EditText edOrderStreet;
    @BindView(R.id.tv_card_price_count)
    TextView tvCardPriceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String cartIds = getIntent().getStringExtra(I.Cart.ID);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_card_pay)
    public void onClick() {
    }
}
