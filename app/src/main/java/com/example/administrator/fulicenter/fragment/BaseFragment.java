package com.example.administrator.fulicenter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/10/19.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        initView();
        initData();
        setListener();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListener();
}
