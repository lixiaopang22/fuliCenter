package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.AlbumsBean;
import com.example.administrator.fulicenter.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.view.FlowIndicator;
import com.example.administrator.fulicenter.view.SlideAutoLoopView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailActivity extends AppCompatActivity {

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

    int goodsId;
    GoodsDetailActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e("main", "goodsID=" + goodsId);
        if(goodsId==0){
            finish();
        }
        mContext=this;
        initView();
        initData();
        setListener();
    }

    private void setListener() {

    }

    private void initView() {

    }

    private void initData() {
        NetDao.downLoadGoodsDetails(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.i("details:"+result);
                if(result!=null){
                    showGoodsDetails(result);
                }else{
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                L.e("details:"+error);
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void showGoodsDetails(GoodsDetailsBean result) {
        tvGoodsEnglishName.setText(result.getGoodsEnglishName());
        tvGoodsNameName.setText(result.getGoodsName());
        tvGoodsCurrentPrice.setText(result.getCurrencyPrice());
        tvGoodsPrice.setText(result.getShopPrice());
        sal.startPlayLoop(indicator,getAlumImagUrl(result),getAlumCount(result));
        wvGoodBrief.loadDataWithBaseURL(null,result.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private int getAlumCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlumImagUrl(GoodsDetailsBean details) {
        String[] urls=new String[]{};
        if(details.getProperties()!=null&&details.getProperties().length>0){
            AlbumsBean[] mAlbumsBeen=details.getProperties()[0].getAlbums();
            urls=new String[mAlbumsBeen.length];
            for (int i=0;i<mAlbumsBeen.length;i++){
                urls[i]=mAlbumsBeen[i].getImgUrl();
            }
        }
        return urls;
    }
}
