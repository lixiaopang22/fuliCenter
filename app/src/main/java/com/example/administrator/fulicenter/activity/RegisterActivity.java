package com.example.administrator.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.Result;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.view.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nickname)
    EditText mNickname;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirmpassword)
    EditText mConfirmpassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    String username;
    String nickname;
    String password;

    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "用户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        username=mUsername.getText().toString().trim();
        nickname=mNickname.getText().toString().trim();
        password=mPassword.getText().toString().trim();
        String confirmPwd=mConfirmpassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            CommonUtils.showShortToast(R.string.user_name_connot_be_empty);
            mUsername.requestFocus();
            return;
        }else if(!username.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showShortToast(R.string.illegal_user_name);
            mUsername.requestFocus();
            return;
        }else if(TextUtils.isEmpty(nickname)){
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            mNickname.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            mPassword.requestFocus();
            return;
        }else if(TextUtils.isEmpty(confirmPwd)){
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            mConfirmpassword.requestFocus();
            return;
        }else if(!password.equals(confirmPwd)){
            CommonUtils.showShortToast(R.string.two_input_password);
            mConfirmpassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd=new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.onRegister(mContext, username, nickname, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if(result==null){
                    CommonUtils.showShortToast(R.string.register_fail);
                }else{
                    if(result.isRetMsg()){
                        CommonUtils.showLongToast(R.string.register_success);
                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,username));
                        MFGT.finish(mContext);
                    }else{
                        CommonUtils.showShortToast(R.string.register_fail_exists);
                        mUsername.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showShortToast(R.string.register_fail);
            }
        });
    }
}
