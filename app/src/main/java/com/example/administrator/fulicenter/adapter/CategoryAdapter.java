package com.example.administrator.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.CategoryChildBean;
import com.example.administrator.fulicenter.bean.CategoryGroupBean;
import com.example.administrator.fulicenter.utils.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList,
                           ArrayList<ArrayList<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList = new ArrayList<>();
        mGroupList.addAll(groupList);
        mChildList = new ArrayList<>();
        mChildList.addAll(childList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ?
                mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ?
                mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanding, View view, ViewGroup viewGroup) {
        GroupViewHolder groupHolder;
        if (view == null) {
            view=View.inflate(mContext, R.layout.item_category_group, null);
            groupHolder = new GroupViewHolder(view);
            view.setTag(groupHolder);
        } else {
            view.getTag();
            groupHolder = (GroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, groupHolder.ivCategoryGroup, group.getImageUrl());
            groupHolder.tvGroupName.setText(group.getName());
            groupHolder.ivdownUp.setImageResource(isExpanding ? R.mipmap.expand_off : R.mipmap.expand_on);

        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childHolder;
        if (view == null) {
            view= View.inflate(mContext, R.layout.item_category_child, null);
            childHolder=new ChildViewHolder(view);
            view.setTag(childHolder);
        } else {
            childHolder= (ChildViewHolder) view.getTag();
        }
        CategoryChildBean child = getChild(groupPosition, childPosition);
        if(child!=null){
            ImageLoader.downloadImg(mContext,childHolder.ivCategoryChild,child.getImageUrl());
            childHolder.tvChildName.setText(child.getName());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupList, ArrayList<ArrayList<CategoryChildBean>> childList) {
        if(mGroupList!=null){
            mGroupList.clear();
        }
        mGroupList.addAll(groupList);
        if(mChildList!=null){
            mChildList.clear();
        }
        mChildList.addAll(childList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        @BindView(R.id.ivCategoryGroup)
        ImageView ivCategoryGroup;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.ivdownUp)
        ImageView ivdownUp;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.ivCategoryChild)
        ImageView ivCategoryChild;
        @BindView(R.id.tv_child_name)
        TextView tvChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
