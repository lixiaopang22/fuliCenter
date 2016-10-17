package com.example.administrator.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.NewGoodsBean;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GoodsAdapter extends Adapter {
    List<NewGoodsBean> nlist;
    Context mContext;

    public GoodsAdapter(Context context, ArrayList<NewGoodsBean> list) {
        mContext=context;
        nlist=new ArrayList<>();
        nlist.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == I.TYPE_FOOTER) {
            viewHolder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            viewHolder = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==1){

        }else{
            GoodsViewHolder vh= (GoodsViewHolder) holder;
            NewGoodsBean goods=nlist.get(position);
            ImageLoader.downloadImg(mContext,vh.ivGoods,goods.getGoodsThumb());
            vh.tvGoodsName.setText(goods.getGoodsName());
            vh.tvGoodsPrice .setText(goods.getCurrencyPrice());
        }
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

    public void initData(ArrayList<NewGoodsBean> list) {
        if(nlist!=null){
            nlist.clear();
        }
        nlist.addAll(list);
        notifyDataSetChanged();
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoods)
        ImageView ivGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.linearLayout_goods)
        LinearLayout linearLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
