package com.taobao.tae.buyingdemo.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.taobao.tae.buyingdemo.R;
import com.taobao.tae.buyingdemo.adapter.CustomFragmentPagerAdapter;
import com.taobao.tae.buyingdemo.constant.AppConfig;
import com.taobao.tae.buyingdemo.constant.MsgConfig;
import com.taobao.tae.buyingdemo.fragment.IndexDefaultFragment;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * author：daijiale
 * date：2015/05/22
 * blog:www.daijiale.cn
 */
public class IndexActivity extends FragmentActivity {

    /*首次点击返回时间*/
    private long firstClickBackTime = 0;

    private static int TOTAL_COUNT = 1;
    /* 用户选择的viewpage下标*/
    public static final String EXTRA_SELECTED_POSITION = "selected_position_index";

    static final int DEFAULT_INDEX = 0;

    private int[] images={R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon};
    private String[] texts={"1","2","3","4","5","6"};
    private GridView mGridView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_layout);
        //传入一张Server上的TAE外链图片
        mImageView=(ImageView) findViewById(R.id.imageViewdemo);
        loadImageByVolley("http://baichuanhacson10.image.alimmdn.com/miaozu.png?t=1432437043000");

        mGridView= (GridView) findViewById(R.id.home_gridview);
        ArrayList<HashMap<String,Object>> mDate=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<6;i++){
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("itemImage",images[i]);
            map.put("itemTexts",texts[i]);
            mDate.add(map);
        }
        SimpleAdapter mAdapter=new SimpleAdapter(this,mDate,
                R.layout.mygridview_layout,new String[]{"itemImage","itemTexts"},
                new int[]{R.id.itemImage,R.id.itemText});
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(IndexActivity.this,DiscussActivity.class);
                intent.putExtra("index",i);
                startActivity(intent);

            }
        });
//        initView();
    }

    /**
     * 初始化页面，采用ViewPager实现，方便后面扩展
     */
    public void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.index_view_pager);
        ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
        Fragment fragment = IndexDefaultFragment.newInstance();
        fragmentsList.add(fragment);
        viewPager.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(DEFAULT_INDEX);
    }

    /**
     * 用户触发返回按钮时的操作
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                long secondClickBackTime = System.currentTimeMillis();
                if (secondClickBackTime - firstClickBackTime > AppConfig.EXIT_CLICK_INTERVAL_TIME) {
                    toast(MsgConfig.CLICK_TO_EXIT_APP);
                    firstClickBackTime = secondClickBackTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 展示一个特定颜色的Toast
     *
     * @param message
     */
    protected void toast(String message) {
        View toastRoot = getLayoutInflater().inflate(R.layout.toast, null);
        Toast toast = new Toast(getApplicationContext());
        toast.setView(toastRoot);
        TextView tv = (TextView) toastRoot.findViewById(R.id.toast_notice);
        tv.setText(message);
        toast.show();
    }

    /**
     * send broadcast that selected fragment have changed, to tell child ViewPager to start or stop auto scroll
     *
     * @param position
     */
    private void sendSelectedBroadcast(int position) {
        Intent i = new Intent();
        i.putExtra(EXTRA_SELECTED_POSITION, position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    public void loadImageByVolley(String imageUrl){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, R.drawable.ic_launcher,R.drawable.ic_launcher);
        imageLoader.get(imageUrl, listener);

    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            sendSelectedBroadcast(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
