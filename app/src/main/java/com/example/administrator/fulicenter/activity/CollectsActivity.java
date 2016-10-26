package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.adapter.CollectsAdapter;
import com.example.administrator.fulicenter.bean.CollectBean;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.ConvertUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.view.DisplayUtils;
import com.example.administrator.fulicenter.view.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectsActivity extends BaseActivity {

    CollectsActivity mContext;
    CollectsAdapter cAdapter;
    ArrayList<CollectBean> mList;
    GridLayoutManager manager;
    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    int pageId=1;
    User user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        mContext = this;
        mList=new ArrayList<>();
        cAdapter=new CollectsAdapter(mContext,mList);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.collects_title));
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        manager = new GridLayoutManager(mContext, I.COLUM_NUM);
        recycleView.setLayoutManager(manager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(cAdapter);
        recycleView.addItemDecoration(new SpaceItemDecoration(12));
    }

    protected void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvFresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCollects(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = manager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == cAdapter.getItemCount() - 1
                        && cAdapter.isMore()) {
                    pageId++;
                    downloadCollects(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = manager.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition == 0);
            }
        });
    }

    private void downloadCollects(final int action) {
        NetDao.downCollects(mContext, user.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                cAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<CollectBean> array2List = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        cAdapter.initData(array2List);
                    } else {
                        cAdapter.addData(array2List);
                    }
                    if (array2List.size() < I.PAGE_SIZE_DEFAULT) {
                        cAdapter.setMore(false);
                    }
                } else {
                    cAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                cAdapter.setMore(true);
                CommonUtils.showLongToast(error);
                L.e("error:" + error);
            }
        });
    }

    @Override
    protected void initData() {
        user= FuLiCenterApplication.getUser();
        if(user==null){
            finish();
        }
        downloadCollects(I.ACTION_DOWNLOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
