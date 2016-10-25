package com.example.administrator.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.Result;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.dao.SharedPreferenceUtils;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.ImageLoader;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.utils.OnSetAvatarListener;
import com.example.administrator.fulicenter.utils.ResultUtils;
import com.example.administrator.fulicenter.view.DisplayUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends BaseActivity {

    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tvUser_name)
    TextView tvUsername;
    @BindView(R.id.tvUser_Nickname)
    TextView tvUserNickname;

    OnSetAvatarListener mOnSetAvatarListener;
    User user = null;
    UserProfileActivity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.user_profile));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
            return;
        }
        showInfo();
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.rl_personal_useravatar, R.id.rl_personal_username, R.id.rl_personal_usernick, R.id.bt_loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_personal_useravatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_upload_avatar,
                        user.getMuserName(), I.AVATAR_TYPE_USER_PATH);
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
        if (user != null) {
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

    private void showInfo() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, ivUserAvatar);
            tvUsername.setText(user.getMuserName());
            tvUserNickname.setText(user.getMuserNick());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult.requestCode" + requestCode, ",onActivityResult.resultCode" + resultCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode, data, ivUserAvatar);
        if (requestCode == I.REQUEST_CODE_NICK) {
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext, user.getMavatarPath() + "/" +
                user.getMuserName() + I.AVATAR_SUFFIX_JPG));
        L.e("file" + file.exists());
        L.e("file" + file.getAbsolutePath());
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.update_user_avatar));
        pd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, User.class);
                if (result == null) {
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                } else {
                    User u = (User) result.getRetData();
                    if (result.isRetMsg()) {
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u), mContext, ivUserAvatar);
                        CommonUtils.showLongToast(R.string.update_user_avatar_success);
                    } else {
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }
}
