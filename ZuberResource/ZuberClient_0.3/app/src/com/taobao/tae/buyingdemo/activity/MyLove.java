package com.taobao.tae.buyingdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.taobao.tae.buyingdemo.R;

/**
 * Created by Administrator on 2015/5/24.
 */
public class MyLove extends Activity {

    private Button mbtn_connect;
    private Button mbtn_giveup;
    private Button mbtn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.mylove_layout);

        mbtn_confirm= (Button) findViewById(R.id.btn_confirm);
        mbtn_connect= (Button) findViewById(R.id.btn_connect);
        mbtn_giveup= (Button) findViewById(R.id.btn_giveup);

        mbtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbtn_giveup.setVisibility(View.GONE);
            }
        });
    }
}
