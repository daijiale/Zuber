package com.taobao.tae.buyingdemo.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.taobao.tae.buyingdemo.R;
import com.taobao.tae.buyingdemo.constant.AppConfig;
import com.taobao.tae.buyingdemo.constant.MsgConfig;
import com.taobao.tae.sdk.TaeSDK;
import com.taobao.tae.sdk.callback.CallbackContext;
import com.taobao.tae.sdk.callback.LoginCallback;
import com.taobao.tae.sdk.model.Session;


/**
 * 个人中心
 * author：daijiale
 * date：2015/05/22
 * blog:www.daijiale.cn
 */
public class MyActivity extends BaseActivity {

    protected Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_layout);
        myActivity = this;
        initView();
        addBtnListener();
    }


    public void initView() {
        if (TaeSDK.getSession().isLogin()) {
            initUserInformation();
        } else {
            toLoginPage();
        }
    }


    /**
     * 采用H5 登录方式登录
     */
    public void toLoginPage() {
        TaeSDK.showLogin(this, new LoginCallback() {
            @Override
            public void onSuccess(Session session) {
                toast(MsgConfig.LOGIN_SUCCESS);
                initUserInformation();
            }

            @Override
            public void onFailure(int i, String s) {
                if (i == 10003) {
                    // 用户取消
                    myActivity.finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConfig.ACTIVITY_NAME_KEY, R.string.activity_name_of_my);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    onLoginFailure();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化登录用户信息
     */
    public void initUserInformation() {
        TextView nickTextView = (TextView) findViewById(R.id.my_nick_txt);
        nickTextView.setText(TaeSDK.getSession().getUser().nick);
        ImageView avatarImageView = (ImageView) findViewById(R.id.my_avatar_img);
        avatarImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_avatar_img));
    }

    /**
     * 登录失败
     */
    public void onLoginFailure() {
        toast(MsgConfig.LOGIN_FAILURE);
    }


    public void addBtnListener() {
        TextView settingTextView = (TextView) findViewById(R.id.personal_center_setting_txt);
        settingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConfig.ACTIVITY_NAME_KEY, R.string.activity_name_of_my);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView settingTextView2= (TextView) findViewById(R.id.personal_center_setting_txt2);
        settingTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MyLove.class);
                startActivity(intent);
            }
        });
    }
}
