package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.AlbumsBean;
import com.example.administrator.fulicenter.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter.bean.MessageBean;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.view.FlowIndicator;
import com.example.administrator.fulicenter.view.SlideAutoLoopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tvGoodsEnglishName)
    TextView tvGoodsEnglishName;
    @BindView(R.id.tvGoodsNameName)
    TextView tvGoodsNameName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsCurrentPrice)
    TextView tvGoodsCurrentPrice;
    @BindView(R.id.sal)
    SlideAutoLoopView sal;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wvGoodBrief)
    WebView wvGoodBrief;
    @BindView(R.id.ivGoodCollect)
    ImageView ivGoodCollect;


    int goodsId;
    GoodsDetailActivity mContext;
    boolean isColleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("main", "goodsID=" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initView() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        isCollected();
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(this);
    }

    @Override
    protected void initData() {
        NetDao.downLoadGoodsDetails(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details:" + result);
                if (result != null) {
                    showGoodsDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details:" + error);
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void showGoodsDetails(GoodsDetailsBean result) {
        tvGoodsEnglishName.setText(result.getGoodsEnglishName());
        tvGoodsNameName.setText(result.getGoodsName());
        tvGoodsCurrentPrice.setText(result.getCurrencyPrice());
        tvGoodsPrice.setText(result.getShopPrice());
        sal.startPlayLoop(indicator, getAlumImagUrl(result), getAlumCount(result));
        wvGoodBrief.loadDataWithBaseURL(null, result.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlumCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlumImagUrl(GoodsDetailsBean details) {
        String[] urls = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] mAlbumsBeen = details.getProperties()[0].getAlbums();
            urls = new String[mAlbumsBeen.length];
            for (int i = 0; i < mAlbumsBeen.length; i++) {
                urls[i] = mAlbumsBeen[i].getImgUrl();
            }
        }
        return urls;
    }

    private void updateStatus() {
        if (isColleted) {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }


    @OnClick(R.id.ivGoodCollect)
    public void onCollected(){
        User user = FuLiCenterApplication.getUser();
        if(user==null){
            MFGT.gotoLogin(mContext);
        }else{
            if(isColleted){
                NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null && result.isSuccess()){
                            isColleted=!isColleted;
                            updateStatus();
                            CommonUtils.showLongToast(result.getMsg());
                        }else{

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
            else{
                NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null && result.isSuccess()){
                            isColleted=!isColleted;
                            updateStatus();
                            CommonUtils.showLongToast(result.getMsg());
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
    private void isCollected() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isColected(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isColleted = true;
                    }else{
                        isColleted=false;
                    }
                    updateStatus();
                }

                @Override
                public void onError(String error) {
                    isColleted=false;
                    updateStatus();
                }
            });
        }
        updateStatus();
    }
}
