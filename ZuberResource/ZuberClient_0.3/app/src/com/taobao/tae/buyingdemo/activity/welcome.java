package com.taobao.tae.buyingdemo.activity;


/**
 * author：daijiale
 * date：2015/05/22
 * blog:www.daijiale.cn
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.taobao.tae.buyingdemo.R;

import java.util.ArrayList;


public class welcome extends Activity {
    private ViewPager mViewpager;
    private Button mButton;
    private ArrayList<View> views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity_layout);

        views=new ArrayList<View>();
        mViewpager=(ViewPager)findViewById(R.id.viewpager);
        LayoutInflater mInflater=getLayoutInflater().from(this);
        View view1=mInflater.inflate(R.layout.viewpager_1,null);
        View view2=mInflater.inflate(R.layout.viewpager_2,null);
        View view3=mInflater.inflate(R.layout.viewpager_3,null);
        mButton=(Button)view3.findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(welcome.this,MainActivity.class));
                welcome.this.finish();
            }
        });
        views.add(view1);
        views.add(view2);
        views.add(view3);


        PagerAdapter adapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view==o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager)container).addView(views.get(position), 0);
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }

            @Override
            public void finishUpdate(ViewGroup container) {

            }
        };

        mViewpager.setAdapter(adapter);
    }
}
