package com.example.administrator.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.CartBean;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.ConvertUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.view.DisplayUtils;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity implements PaymentHandler{

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
    ArrayList<CartBean> mList;
    User user = null;
    String cartIds = "";
    OrderActivity mContext;
    String[] ids = new String[]{};
    private static final String TAG = OrderActivity.class.getSimpleName();
    private static String URL = "http://218.244.151.190/demo/charge";
    int ranPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        mList = new ArrayList<>();
        mContext = this;
        super.onCreate(savedInstanceState);
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getString(R.string.confirm_order));
    }

    @Override
    protected void initData() {
        cartIds = getIntent().getStringExtra(I.Cart.ID);
        L.e(TAG, "cartIds=" + cartIds);
        user = FuLiCenterApplication.getUser();
        if (cartIds == null || cartIds.equals("") || user == null) {
            finish();
        }
        ids = cartIds.split(",");
        getOrderList();
    }

    private void getOrderList() {
        NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                ArrayList<CartBean> array2List = ConvertUtils.array2List(result);
                if (result == null || result.length == 0) {
                    finish();
                } else {
                    mList.addAll(array2List);
                    sumPrice();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void sumPrice() {
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                for (String id : ids) {
                    if (id.equals(String.valueOf(c.getId()))) {
                        ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                        L.e(TAG, "ranPrice=" + ranPrice);
                    }
                }
            }
        }
        tvCardPriceCount.setText("合计:￥" + Double.valueOf(ranPrice) + "");
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_card_pay)
    public void onClick() {
        String receiveName=edOrderName.getText().toString();
        if(TextUtils.isEmpty(receiveName)){
            edOrderName.setError("收货人姓名不能为空");
            edOrderName.requestFocus();
            return;
        }
        String phone=edOrderPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            edOrderPhone.setError("手机号码不能为空");
            edOrderPhone.requestFocus();
            return;
        }if(!phone.matches("\\d{11}")){
            edOrderPhone.setError("手机号码格式错误");
            edOrderPhone.requestFocus();
            return;
        }
        String area=spinOrderProvince.getSelectedItem().toString();
        if(TextUtils.isEmpty(area)){
            Toast.makeText(OrderActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String street=edOrderStreet.getText().toString();
        if(TextUtils.isEmpty(street)){
            edOrderStreet.setError("收货地址不能为空");
            edOrderStreet.requestFocus();
            return;
        }
        gotoPay();
    }

    private void gotoPay() {
            // 产生个订单号
            String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                    .format(new Date());

            // 计算总金额（以分为单位）
            // 构建账单json对象
            JSONObject bill = new JSONObject();

            // 自定义的额外信息 选填
            JSONObject extras = new JSONObject();
            try {
                extras.put("extra1", "extra1");
                extras.put("extra2", "extra2");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                bill.put("order_no", orderNo);
                bill.put("amount", ranPrice*100);
                bill.put("extras", extras);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //壹收款: 创建支付通道的对话框
            PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
        }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

