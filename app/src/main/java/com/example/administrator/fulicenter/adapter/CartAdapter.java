package com.example.administrator.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.CartBean;
import com.example.administrator.fulicenter.bean.GoodsDetailsBean;
import com.example.administrator.fulicenter.bean.MessageBean;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;



    public CartAdapter(Context context, ArrayList<CartBean> list) {
        mContext = context;
//        mList = new ArrayList<>();
//        mList.addAll(list);
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        holder = new CardViewHolder(View.inflate(mContext, R.layout.iterm_cart_1, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardViewHolder ch = (CardViewHolder) holder;
        final CartBean mCartBean = mList.get(position);
        GoodsDetailsBean goods = mCartBean.getGoods();
        if (goods != null) {
            ImageLoader.downloadImg(mContext, ch.ivCardGoodsImage, goods.getGoodsThumb());
            ch.ivCardGoodsName.setText(goods.getGoodsName());
            ch.tvCardGoodsPrice.setText(goods.getCurrencyPrice());
        }
        ch.tvCardGoodsCount.setText("(" + mCartBean.getCount() + ")");
        ch.cbCardChecked.setChecked(mCartBean.isChecked());
        ch.cbCardChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                mCartBean.setChecked(b);
                mContext.sendBroadcast(new Intent(I.CARD_UPDATE_BROADCAST));
            }
        });
        ch.rlLayoutCard.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> list) {
//        if (mList != null) {
//            mList.clear();
//        }
//        mList.addAll(list);
        mList = list;
        notifyDataSetChanged();
    }


    class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_card_checked)
        CheckBox cbCardChecked;
        @BindView(R.id.iv_card_goods_image)
        ImageView ivCardGoodsImage;
        @BindView(R.id.iv_card_goodsName)
        TextView ivCardGoodsName;
        @BindView(R.id.iv_card_add)
        ImageView ivCardAdd;
        @BindView(R.id.tv_card_goods_count)
        TextView tvCardGoodsCount;
        @BindView(R.id.iv_card_del)
        ImageView ivCardDel;
        @BindView(R.id.tv_card_goods_price)
        TextView tvCardGoodsPrice;
        @BindView(R.id.rl_layout_card)
        RelativeLayout rlLayoutCard;

        CardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick({R.id.iv_card_goods_image,R.id.tv_card_goods_price,R.id.iv_card_goodsName})
        public void gotoDetail(){
            final int postion = (int) rlLayoutCard.getTag();
            final CartBean cart = mList.get(postion);
            MFGT.goGoodsDetailActivity(mContext,cart.getGoodsId());
        }

        @OnClick({R.id.iv_card_add, R.id.iv_card_del})
        public void onClick(View view) {
            final int postion = (int) rlLayoutCard.getTag();
            final CartBean cart = mList.get(postion);
            switch (view.getId()) {
                case R.id.iv_card_add:
                    NetDao.updateCartCount(mContext, cart.getId(), cart.getCount(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mList.get(postion).setCount(mList.get(postion).getCount() + 1);
                                mContext.sendBroadcast(new Intent(I.CARD_UPDATE_BROADCAST));
                                tvCardGoodsCount.setText("(" + mList.get(postion).getCount() + ")");
                            }
                        }

                        @Override
                        public void onError(String error) {
                            L.e("error=" + error);
                        }
                    });
                    break;
                case R.id.iv_card_del:
                    if (cart.getCount() > 1) {
                        NetDao.updateCartCount(mContext, cart.getId(), cart.getCount(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    mList.get(postion).setCount(mList.get(postion).getCount() - 1);
                                    mContext.sendBroadcast(new Intent(I.CARD_UPDATE_BROADCAST));
                                    tvCardGoodsCount.setText("(" + mList.get(postion).getCount() + ")");
                                }
                            }

                            @Override
                            public void onError(String error) {
                                L.e("error=" + error);
                            }
                        });
                    } else {
                        NetDao.deleteCartGoods(mContext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    mList.remove(postion);
                                    mContext.sendBroadcast(new Intent(I.CARD_UPDATE_BROADCAST));
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                L.e("error=" + error);
                            }
                        });
                    }
                    break;
            }
        }
    }
}

