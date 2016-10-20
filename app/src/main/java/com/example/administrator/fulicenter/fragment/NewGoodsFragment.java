package com.example.administrator.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.activity.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    MainActivity mContext;
    GoodsAdapter gAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager manager;

    @BindView(R.id.tvFresh)
    TextView tvFresh;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    int pageId=1;
    public NewGoodsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getContext();
        mList=new ArrayList<>();
        gAdapter=new GoodsAdapter(mContext,mList);
        initView();
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullDownListener() {
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    srl.setRefreshing(true);
                    tvFresh.setVisibility(View.VISIBLE);
                    pageId=1;
                    downloadNewGoods(I.ACTION_PULL_DOWN);
                }
            });
    }
    private void setPullUpListener() {
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = manager.findLastVisibleItemPosition();
                if(newState==RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition==gAdapter.getItemCount()-1
                        && gAdapter.isMore()){
                    pageId++;
                    downloadNewGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = manager.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition==0);
            }
        });
    }

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mContext,I.CAT_ID, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                gAdapter.setMore(true);
                if(result!=null&&result.length>0){
                    ArrayList<NewGoodsBean> array2List = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN){
                        gAdapter.initData(array2List);
                    }else{
                        gAdapter.addData(array2List);
                    }
                    if(array2List.size()<I.PAGE_SIZE_DEFAULT){
                        gAdapter.setMore(false);
                    }
                }
                else {
                    gAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvFresh.setVisibility(View.GONE);
                gAdapter.setMore(true);
                CommonUtils.showLongToast(error);
                L.e("error:"+error);
            }
        });
    }

    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        manager=new GridLayoutManager(mContext, I.COLUM_NUM);
        recycleView.setLayoutManager(manager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(gAdapter);
        recycleView.addItemDecoration(new SpaceItemDecoration(12));
    }

}
