package com.example.administrator.fulicenter.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.activity.MainActivity;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends BaseFragment {

    private static String TAG=PersonalCenterFragment.class.getSimpleName();
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    MainActivity mContext;

    public PersonalCenterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        mContext= (MainActivity) getActivity();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        User user = FuLiCenterApplication.getUser();
        L.e(TAG,"user"+user);
        if(user==null){
            MFGT.gotoLogin(mContext);
        }else{
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        User user = FuLiCenterApplication.getUser();
        L.e(TAG,"user"+user);
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserNick());
        }
    }

    @OnClick({R.id.tv_center_settings,R.id.center_user_info})
    public void onClick() {
        MFGT.gotoSetting(mContext);
    }
}
