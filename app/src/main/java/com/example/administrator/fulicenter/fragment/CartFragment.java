package com.example.administrator.fulicenter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.activity.MainActivity;
import com.example.administrator.fulicenter.adapter.CartAdapter;
import com.example.administrator.fulicenter.bean.CartBean;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.ConvertUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.view.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/19.
 */
public class CartFragment extends BaseFragment {
    private static final String TAG = CartFragment.class.getSimpleName();
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    @BindView(R.id.sfl)
    SwipeRefreshLayout sfl;
    @BindView(R.id.tv_card_price_count)
    TextView tvCardPriceCount;
    @BindView(R.id.tv_card_goods_price_Count)
    TextView tvCardGoodsPriceCount;
    @BindView(R.id.tv_card_save_price_count)
    TextView tvCardranPriceCount;
    @BindView(R.id.tv_card_save_goods_price_Count)
    TextView tvCardSaveGoodsPriceCount;
    @BindView(R.id.bt_card_pay)
    Button btCardPay;
    @BindView(R.id.rl_card_pay_price)
    RelativeLayout rlCardPayPrice;
    @BindView(R.id.tv_card_nothing)
    TextView tvCardNothing;

    String cartIds="";
    LinearLayoutManager mLinearLayoutManager;
    MainActivity mContext;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    UpdateCardPrice mUpdateCardPrice;
    public CartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void setListener() {
        setPullDownListener();
        mUpdateCardPrice = new UpdateCardPrice();
        IntentFilter filter = new IntentFilter(I.CARD_UPDATE_BROADCAST);
        mContext.registerReceiver(mUpdateCardPrice, filter);
    }

    private void setPullDownListener() {
        sfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sfl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downloadCart();
            }
        });
    }

    @Override
    protected void initData() {
        downloadCart();
    }

    private void downloadCart() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.downloadCart(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    L.e(TAG, "result=" + result.length);
                    sfl.setRefreshing(false);//设置是否刷新
                    tvRefresh.setVisibility(View.GONE);//隐藏刷新提示
                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        mList.clear();
                        mList = list;
                        mAdapter.initData(mList);
                        setCardLayout(true);
                    }else {
                        setCardLayout(false);
                    }
                }

                @Override
                public void onError(String error) {
                    sfl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    CommonUtils.showLongToast(error);
                    L.e("error: " + error);
                }
            });
        }
    }

    @Override
    protected void initView() {

        sfl.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green));
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        rlv.setLayoutManager(mLinearLayoutManager);
        rlv.setHasFixedSize(true);
        rlv.setAdapter(mAdapter);
        rlv.addItemDecoration(new SpaceItemDecoration(10));
        setCardLayout(false);
        sumPrice();
    }

    private void setCardLayout(boolean hasCard) {
        tvCardNothing.setVisibility(hasCard ? View.GONE : View.VISIBLE);
        rlCardPayPrice.setVisibility(hasCard ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.bt_card_pay)
    public void onClick() {
        if(cartIds!=null && cartIds.length()>0 && !cartIds.equals("")){
            MFGT.gotoBuy(mContext,cartIds);
        }else{
            CommonUtils.showLongToast(R.string.order_nothing);
        }
    }

    private void sumPrice() {
        cartIds="";
        int sumPrice = 0;
        int ranPrice = 0;
        if (mList != null && mList.size() > 0) {
            for (CartBean c : mList) {
                if (c.isChecked()) {
                    cartIds+=c.getId()+",";
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    L.e(TAG, "sumPrice=" + sumPrice);
                    ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                    L.e(TAG, "ranPrice=" + ranPrice);
                }
            }
            tvCardGoodsPriceCount.setText(Double.valueOf(ranPrice) + "");
            tvCardSaveGoodsPriceCount.setText(Double.valueOf(sumPrice - ranPrice) + "");
        } else {
            cartIds="";
            tvCardGoodsPriceCount.setText(String.valueOf(0));
            tvCardSaveGoodsPriceCount.setText(String.valueOf(0));
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    class UpdateCardPrice extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.e(TAG, "UpdateCardPriceReceiver....");
            sumPrice();
            L.e(TAG, "mList="+mList.size());
            setCardLayout(mList != null && mList.size() > 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mUpdateCardPrice);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
