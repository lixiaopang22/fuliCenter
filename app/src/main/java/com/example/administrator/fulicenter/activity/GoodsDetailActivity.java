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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
    @BindView(R.id.ivGoodCart)
    ImageView ivGoodCart;

    @OnClick(R.id.ivGoodCart)
    public void addCart(){
        User user = FuLiCenterApplication.getUser();
        if(user!=null) {
            NetDao.addCar(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null &&result.isSuccess()){
                        CommonUtils.showLongToast(R.string.add_cart_sucess);
                    }else{
                        CommonUtils.showLongToast(R.string.add_cart_fail);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }else{
            MFGT.gotoLogin(mContext);
        }

    }

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
    public void onCollected() {
        User user = FuLiCenterApplication.getUser();
        if (user == null) {
            MFGT.gotoLogin(mContext);
        } else {
            if (isColleted) {
                NetDao.deleteCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isColleted = !isColleted;
                            updateStatus();
                            CommonUtils.showLongToast(result.getMsg());
                        } else {

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.addCollect(mContext, user.getMuserName(), goodsId, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            isColleted = !isColleted;
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
                    } else {
                        isColleted = false;
                    }
                    updateStatus();
                }

                @Override
                public void onError(String error) {
                    isColleted = false;
                    updateStatus();
                }
            });
        }
        updateStatus();
    }

    @OnClick(R.id.ivGoodShare)
    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
