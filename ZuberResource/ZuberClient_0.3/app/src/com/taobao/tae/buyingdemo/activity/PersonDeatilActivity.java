package com.taobao.tae.buyingdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.taobao.tae.buyingdemo.R;



/**
 * author：daijiale
 * date：2015/05/22
 * blog:www.daijiale.cn
 */
public class PersonDeatilActivity extends Activity {
    private TextView headText;
    private TextView personNameText;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.detail_person_layout);
        headText= (TextView) findViewById(R.id.detail_person_headText);
        personNameText =(TextView)findViewById(R.id.detail_name);
        view=findViewById(R.id.person_choice_one);
        String headtextfrom=getIntent().getExtras().getString("headTitle");
        headText.setText(headtextfrom);
        personNameText.setText(headtextfrom);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonDeatilActivity.this,ChoiceActivity.class));
            }
        });
    }
}
