package com.example.administrator.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.adapter.GoodsAdapter;
import com.example.administrator.fulicenter.bean.NewGoodsBean;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.ConvertUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.view.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    GoodsAdapter  gAdapter;
    CategoryActivity mContext;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager manager;

    int pageId;
    int catId;
    @BindView(R.id.sort_price)
    Button sortPrice;
    @BindView(R.id.sort_addTime)
    Button sortAddTime;

    boolean addTimeAsc=false;
    boolean priceAce=false;
    int sortBy=I.SORT_BY_ADDTIME_DESC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        gAdapter = new GoodsAdapter(mContext, mList);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        if (catId == 0) {
            finish();
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        manager = new GridLayoutManager(mContext, I.COLUM_NUM);
        recycleView.setLayoutManager(manager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(gAdapter);
        recycleView.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
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
                downLoadCategoryGoods(I.ACTION_PULL_DOWN);
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
                        && lastPosition == gAdapter.getItemCount() - 1
                        && gAdapter.isMore()) {
                    pageId++;
                    downLoadCategoryGoods(I.ACTION_PULL_UP);
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

    private void downLoadCategoryGoods(final int action) {
        NetDao.downLoadCategoryGoods(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                gAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> array2List = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        gAdapter.initData(array2List);
                    } else {
                        gAdapter.addData(array2List);
                    }
                    if (array2List.size() < I.PAGE_SIZE_DEFAULT) {
                        gAdapter.setMore(false);
                    }
                } else {
                    gAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                gAdapter.setMore(true);
                CommonUtils.showLongToast(error);
                L.e("error:" + error);
            }
        });
    }

    @Override
    protected void initData() {
        downLoadCategoryGoods(I.ACTION_DOWNLOAD);
    }

    @OnClick({R.id.sort_price, R.id.sort_addTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_price:
                if(priceAce){
                    sortBy=I.SORT_BY_ADDTIME_ASC;
                }else{
                    sortBy=I.SORT_BY_ADDTIME_DESC;
                }
                priceAce=!priceAce;
                break;
            case R.id.sort_addTime:
                if(addTimeAsc){
                    sortBy=I.SORT_BY_ADDTIME_ASC;
                }else{
                    sortBy=I.SORT_BY_ADDTIME_DESC;
                }
                addTimeAsc=!addTimeAsc;
                break;
        }
        gAdapter.setSortBy(sortBy);
    }
}
