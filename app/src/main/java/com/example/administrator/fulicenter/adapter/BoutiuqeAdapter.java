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

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.BoutiqueBean;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.view.FooterViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BoutiuqeAdapter extends Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mlist;

    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public BoutiuqeAdapter(Context context, ArrayList<BoutiqueBean> list) {
        mContext=context;
        mlist=new ArrayList<>();
        mlist.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == I.TYPE_FOOTER) {
            viewHolder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            viewHolder = new BoutiqueViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvFooter.setText(getFooterString());
        }
        if (holder instanceof BoutiqueViewHolder) {
            BoutiqueBean boutiqueBean = mlist.get(position);
            ImageLoader.downloadImg(mContext,((BoutiqueViewHolder) holder).ivBoutiqueImg,boutiqueBean.getImageurl());
            ((BoutiqueViewHolder) holder).tvBoutiqueTitle.setText(boutiqueBean.getTitle());
            ((BoutiqueViewHolder) holder).tvBoutiqueName.setText(boutiqueBean.getName());
            ((BoutiqueViewHolder) holder).tvDescription.setText(boutiqueBean.getDescription());

        }
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.layout_boutique)
        RelativeLayout layoutBoutique;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
