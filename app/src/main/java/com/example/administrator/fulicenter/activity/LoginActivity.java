package com.example.administrator.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.Result;
import com.example.administrator.fulicenter.bean.User;
import com.example.administrator.fulicenter.dao.SharedPreferenceUtils;
import com.example.administrator.fulicenter.dao.UserDao;
import com.example.administrator.fulicenter.net.NetDao;
import com.example.administrator.fulicenter.net.OkHttpUtils;
import com.example.administrator.fulicenter.utils.CommonUtils;
import com.example.administrator.fulicenter.utils.I;
import com.example.administrator.fulicenter.utils.L;
import com.example.administrator.fulicenter.utils.MFGT;
import com.example.administrator.fulicenter.utils.ResultUtils;
import com.example.administrator.fulicenter.view.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;

    String username;
    String password;
    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        DisplayUtils.initBack(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                checkedInput();
                break;
            case R.id.btn_register:
                MFGT.getoRegister(this);
                break;
        }
    }

    private void checkedInput() {
        username=mUsername.getText().toString().trim();
        password=mPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            mUsername.requestFocus();
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            mPassword.requestFocus();
            return;
        }
        login();
    }

    private void login() {
        final ProgressDialog pd=new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        NetDao.onLogin(mContext, username, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str,User.class);
                if(result==null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else{
                    if (result.isRetMsg()) {
                        L.e("登陆成功");
                        User user=(User)result.getRetData();
                        UserDao dao=new UserDao(mContext);
                        boolean success = dao.saveUser(user);
                        if(success){
                            L.e("success");
                            SharedPreferenceUtils.getInstance(mContext).saveUser(user.getMuserName());
                            FuLiCenterApplication.setUser(user);
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }else if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                        CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                    }else {
                        if(result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                        }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else{
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== I.REQUEST_CODE_REGISTER){
            String name = data.getStringExtra(I.User.USER_NAME);
            mUsername.setText(name);
        }
    }
}
