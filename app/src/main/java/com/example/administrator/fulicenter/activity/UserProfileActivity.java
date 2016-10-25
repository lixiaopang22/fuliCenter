package com.example.administrator.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.dao.SharedPreferenceUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.view.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends BaseActivity {

    @BindView(R.id.ivUser_Image)
    ImageView ivUserImage;
    @BindView(R.id.tvUser_name)
    TextView tvUsername;
    @BindView(R.id.tvUser_Nickname)
    TextView tvUserNickname;

    User user=null;
    UserProfileActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.user_profile));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if(user==null){
            finish();
            return;
        }
        showInfo();
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.rl_personal_useravatar, R.id.rl_personal_username, R.id.rl_personal_usernick,R.id.bt_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_personal_useravatar:
                break;
            case R.id.rl_personal_username:
                CommonUtils.showLongToast(R.string.user_name_cannot_be_modify);
                break;
            case R.id.rl_personal_usernick:
                MFGT.gotoUpdateNick(mContext);
                break;
            case R.id.bt_loginout:
                logout();
                break;
        }
    }

    private void logout() {
        if (user!=null) {
            SharedPreferenceUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLogin(mContext);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }

    private void showInfo(){
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserImage);
            tvUsername.setText(user.getMuserName());
            tvUserNickname.setText(user.getMuserNick());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK && resultCode== I.REQUEST_CODE_NICK){
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
    }

}
