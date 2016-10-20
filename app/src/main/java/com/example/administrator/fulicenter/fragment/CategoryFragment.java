package com.example.administrator.fulicenter.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.activity.MainActivity;
import com.example.administrator.fulicenter.adapter.CategoryAdapter;
import com.example.administrator.fulicenter.bean.CategoryChildBean;
import com.example.administrator.fulicenter.bean.CategoryGroupBean;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.ConvertUtils;
import com.example.administrator.fulicenter.utils.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends BaseFragment {


    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    CategoryAdapter cAdapter;
    MainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    int groupCount;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getContext();
        mGroupList=new ArrayList<>();
        mChildList=new ArrayList<>();
        cAdapter=new CategoryAdapter(mContext,mGroupList,mChildList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {
        elvCategory.setGroupIndicator(null);
        elvCategory.setAdapter(cAdapter);
    }

    @Override
    protected void initData() {
        downLoadGroup();
    }

    private void downLoadGroup() {
        NetDao.downLoadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if(result!=null&&result.length>0) {
                    ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                    mGroupList.addAll(groupList);
                    for(int i=0;i<groupList.size();i++){
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        CategoryGroupBean c=groupList.get(i);
                        downLoadChild(c.getId(),i);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void downLoadChild(final int id, final int index) {
         NetDao.downLoadCategoryChild(mContext, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
             @Override
             public void onSuccess(CategoryChildBean[] result) {
                 groupCount++;
                 if(result!=null&&result.length>0){
                     ArrayList<CategoryChildBean> childList = ConvertUtils.array2List(result);
                     mChildList.set(index,childList);
                     L.e("childList"+childList.size());
                 }
                 if(groupCount==mGroupList.size()){
                     cAdapter.initData(mGroupList,mChildList);
                 }
             }

             @Override
             public void onError(String error) {

             }
         });
    }

    @Override
    protected void setListener() {

    }

}
