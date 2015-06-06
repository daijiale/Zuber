package com.taobao.tae.buyingdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.taobao.tae.buyingdemo.R;

import java.net.URL;

/**
 * Created by Administrator on 2015/5/24.
 */
public class DiscussActivity extends Activity{

    private ListView mListView;
    private WebView mWebview;
    private Button mbutton;

    private String[] discuss_string={"这个很不错","这个很不错","这个很不错","这个很不错","这个很不错","这个很不错",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discuss_layout);
        mListView= (ListView) findViewById(R.id.discuss_listview);
        mListView.setAdapter(new ArrayAdapter<String>(DiscussActivity.this,R.layout.single_listview_normal,discuss_string));
        mWebview= (WebView) findViewById(R.id.discuss_webview);
        mbutton= (Button) findViewById(R.id.discuss_button);
//        URL url=new URL("http://1432300943990-1.wx.jaeapp.com/store/webview.html);
        mWebview.loadUrl("http://1432300943990-1.wx.jaeapp.com/store/webview.html");
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiscussActivity.this,PersonDeatilActivity.class));
                DiscussActivity.this.finish();
            }
        });
    }
}
