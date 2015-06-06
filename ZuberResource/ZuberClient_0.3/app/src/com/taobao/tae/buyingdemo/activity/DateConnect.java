package com.taobao.tae.buyingdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.taobao.tae.buyingdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 与Server数据交互类
* author：daijiale
* date：2015/05/22
* blog:www.daijiale.cn
*/
public class DateConnect extends Activity {
    private ImageView mImageView;
    private NetworkImageView mNetworkImageView;
    private TextView mTextView;
    //导入Json外联地址
    public String mNationDateUrl = "http://private-73ca6-zuber.apiary-mock.com/nation";
    //导入图床地址
    private String mTOPPic="http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";

    //定义json缓存变量




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        init();
    }

    private void init(){
//        mImageView=(ImageView) findViewById(R.id.imageView);
//        mNetworkImageView=(NetworkImageView)findViewById(R.id.networkImageView);
//        mTextView=(TextView)findViewById(R.id.mTextView);


        RequestQueue queue = Volley.newRequestQueue(this);
        getJSONByVolley(mNationDateUrl);
        // loadImageByVolley(mTOPPic);
        showImageByNetworkImageView(mTOPPic);

    }

    /**
     * 利用Volley获取JSON数据
     */
    public void getJSONByVolley(String JSONDataUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = ProgressDialog.show(this, "族宝", "正在努力加载中");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                JSONDataUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //只传一个nation的Json对象，所以在Volley中调用Json解析函数
                        ParseJsonMulti(response.toString(),"nation","nationlist");
                        if (progressDialog.isShowing()&&progressDialog!=null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        System.out.println("sorry,Error");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    /**
     * 利用Volley异步加载图片
     *
     * 注意方法参数:
     * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
     * 第一个参数:显示图片的ImageView
     * 第二个参数:默认显示的图片资源
     * 第三个参数:加载错误时显示的图片资源
     */
    public void loadImageByVolley(String imageUrl){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageCache imageCache = new ImageCache() {
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
        ImageListener listener = ImageLoader.getImageListener(mImageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(imageUrl, listener);
    }

    /**
     * 利用NetworkImageView显示网络图片
     */
    public void showImageByNetworkImageView(String imageUrl){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageCache imageCache = new ImageCache() {
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
        mNetworkImageView.setTag("url");
        mNetworkImageView.setImageUrl(imageUrl,imageLoader);
    }

    //解析JSON数组串
    private void ParseJsonMulti(String strResult,String mJsonObject,String mJsonArrayName) {
        try {
            JSONObject jsonObjs = new JSONObject(strResult).getJSONObject(mJsonObject);
            JSONArray jsonArray = jsonObjs.getJSONArray(mJsonArrayName);

//            for (int i = 0; i < jsonObjs.length(); i++) {
//                JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
//
//               mTextView.setText(jsonObject2.getString("name"));
//            }

               JSONObject jsonObject_Name2 = (JSONObject)jsonArray.opt(2);

               mTextView.setText(jsonObject_Name2.getString("name"));

        } catch (JSONException e) {
            System.out.println("Jsons Parse Error !");
            e.printStackTrace();
            }
        }

    }
