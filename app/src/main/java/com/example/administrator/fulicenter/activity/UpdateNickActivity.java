package com.example.administrator.fulicenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import com.example.administrator.fulicenter.FuLiCenterApplication;
import com.example.administrator.fulicenter.R;
import com.example.administrator.fulicenter.bean.Result;
import com.example.administrator.fulicenter.bean.User;
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

public class UpdateNickActivity extends BaseActivity {

    @BindView(R.id.etNickName)
    EditText etNickName;

    UpdateNickActivity mContext;
    User user=null;
    private static final String TAG=UpdateNickActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.update_user_nick));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            etNickName.setText(user.getMuserNick());
            etNickName.setSelectAllOnFocus(true);
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_save)
    public void changedNick() {
        if(user!=null){
            String nick=etNickName.getText().toString().trim();
            if(nick.equals(user.getMuserNick())){
                CommonUtils.showLongToast(R.string.update_nick_fail_modify);
            }else if(nick.isEmpty()){
                CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            }else {
                updateNick(nick);
            }
        }
    }
    //更新昵称
    private void updateNick(String nick) {
        final ProgressDialog pd=new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.update_user_nick));
        pd.show();
        NetDao.updateNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str,User.class);
                if(result==null){
                    CommonUtils.showLongToast(R.string.update_fail);
                }else{
                    if (result.isRetMsg()) {
                        L.e("登陆成功");
                        User u=(User)result.getRetData();
                        UserDao dao=new UserDao(mContext);
                        boolean success = dao.saveUser(u);
                        if(success){
                            L.e("success");
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            MFGT.finish(mContext);
                        }else{
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }else if(result.getRetCode()== I.MSG_USER_SAME_NICK){
                        CommonUtils.showLongToast(R.string.nick_name_fail_unmodify);
                    }else {
                        if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                            CommonUtils.showLongToast(R.string.update_fail);
                        }else{
                            CommonUtils.showLongToast(R.string.update_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }

}
