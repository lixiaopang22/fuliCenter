package com.example.administrator.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.CollectBean;
import com.example.administrator.fulicenter.bean.MessageBean;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.view.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CollectsAdapter extends Adapter {
    List<CollectBean> nlist;
    Context mContext;
    boolean isMore;
    int sortBy=I.SORT_BY_ADDTIME_DESC;

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CollectsAdapter(Context context, ArrayList<CollectBean> list) {
        mContext=context;
        nlist=new ArrayList<>();
        nlist.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == I.TYPE_FOOTER) {
            viewHolder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            viewHolder = new CollectsViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_collects, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder= (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFootString());
        }else{
            CollectsViewHolder vh= (CollectsViewHolder) holder;
            CollectBean goods=nlist.get(position);
            ImageLoader.downloadImg(mContext,vh.ivGoods,goods.getGoodsThumb());
            vh.tvGoodsName.setText(goods.getGoodsName());
            vh.mLayoutGoods.setTag(goods);
        }
    }

    private int getFootString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return nlist != null ? nlist.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> array2List) {
        if (nlist!=null){
            nlist.clear();
        }
        nlist.addAll(array2List);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> array2List) {
        nlist.addAll(array2List);
        notifyDataSetChanged();
    }


     class CollectsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoods)
        ImageView ivGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.ivDelete)
        ImageView ivCollectsDelete;
        @BindView(R.id.linearLayout_goods)
        RelativeLayout mLayoutGoods;

         CollectsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.linearLayout_goods)
        public void OnItemClick(){
            CollectBean goods = (CollectBean) mLayoutGoods.getTag();
            MFGT.goGoodsDetailActivity(mContext,goods.getGoodsId());
        }
         @OnClick(R.id.ivDelete)
         public void deleteGoods(){
             final CollectBean goods = (CollectBean) mLayoutGoods.getTag();
             String muserName = FuLiCenterApplication.getUser().getMuserName();
             NetDao.deleteCollect(mContext, muserName, goods.getGoodsId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                 @Override
                 public void onSuccess(MessageBean result) {
                     if(result!=null&&result.isSuccess()){
                         nlist.remove(goods);
                         notifyDataSetChanged();
                     }
                 }

                 @Override
                 public void onError(String error) {

                 }
             });
         }
    }
}

