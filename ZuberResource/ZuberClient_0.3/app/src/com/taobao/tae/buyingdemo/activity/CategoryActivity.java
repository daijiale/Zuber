package com.taobao.tae.buyingdemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.demo.common.AndroidSecretUtil;
import com.alibaba.demo.common.Parameter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.taobao.tae.buyingdemo.R;
import com.taobao.tae.buyingdemo.adapter.ParentCategoryAdapter;
import com.taobao.tae.buyingdemo.constant.ApiConfig;
import com.taobao.tae.buyingdemo.constant.AppConfig;
import com.taobao.tae.buyingdemo.constant.MsgConfig;
import com.taobao.tae.buyingdemo.fragment.ChildCategoryFragment;
import com.taobao.tae.buyingdemo.model.CategoryDO;
import com.taobao.tae.buyingdemo.model.ItemDataObject;
import com.taobao.tae.buyingdemo.util.NetWorkStateUtil;
import com.taobao.tae.buyingdemo.util.VolleySingleton;
import com.taobao.tae.buyingdemo.view.ParentCategoryListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author：daijiale
 * date：2015/05/22
 * blog:www.daijiale.cn
 */
public class CategoryActivity extends FragmentActivity {

    public String TAG = CategoryActivity.class.getName();
    private Context context;
    private RequestQueue requestQueue;
    private ParentCategoryAdapter parentCategoryAdapter;
    private ParentCategoryListView parentCategoryListView;
    //父分类数据
    private List<ItemDataObject> parentItemDataObjectList;
    //子分类Fragment
    private Map<Integer, ChildCategoryFragment> childFragmentMap;
    //当前选中的父分类下标
    private int currentSelectedParentCategoryIndex;
    private int lastSelectedChildFragmentIndex = 0;
    private static final String[] allnation={"请选择","","苗族","傣族"};
    private int[] mDrawable={R.drawable.image2,R.drawable.search_icon,R.drawable.search_icon};
    //server端json地址
    public String mNationDateUrl = "http://private-73ca6-zuber.apiary-mock.com/nation";
    //手工师傅对象数据通过Json传值：
    public static String shifu1;
    private String shifu2;
    private String shifu3;
    private String[] personText = {"戴嘉乐","周晨光","崔巍"};
    private String[] cityText = {"四川雅安","四川郫县","西藏拉萨"};
    private String[] YearText = {"从业50年","从业10年","从业30年"};
    private String[] NumText = {"已制作30件","已制作60件","已制作58件"};
    private personAdapter personadapter;
    private ArrayAdapter<String> nationAdapter;
    private Spinner spinner;
    private ListView mLisrView_right;
    private ListView mListView_left;
    private TextView customized_tv;
    private TextView test_tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customized);
        getJSONByVolley(mNationDateUrl);
        spinner=(Spinner)findViewById(R.id.customized_spinner);
        mListView_left= (ListView) findViewById(R.id.listview_left);
        mLisrView_right=(ListView)findViewById(R.id.customized_listview);
        customized_tv= (TextView) findViewById(R.id.customized_tv);
        test_tv= (TextView) findViewById(R.id.test_tv);
        mListView_left.setAdapter(new ArrayAdapter<String>(this,R.layout.single_listview,allnation));
        nationAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,allnation);
        nationAdapter.setDropDownViewResource(R.layout.myspinner_dropdown);
        spinner.setAdapter(nationAdapter);
        context = getApplicationContext();
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        childFragmentMap = new HashMap<Integer, ChildCategoryFragment>();


//        initParentCategories();
//        addListener();
        mListView_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    return;
                }else{
                    String nation=allnation[i];
                    customized_tv.setText(nation);
                    loadListViewByNation(nation);}
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    return;
                }else{
                    String nation=allnation[i];
                    loadListViewByNation(nation);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /**
     * 初始化父分类
     */
    public void initParentCategories() {
        parentCategoryListView = (ParentCategoryListView) findViewById(R.id.category_tree_list);
        parentCategoryAdapter = new ParentCategoryAdapter(this);
        parentCategoryListView.setAdapter(parentCategoryAdapter);
        getParentCategories();
        parentCategoryOnClick();
        RequestQueue queue = Volley.newRequestQueue(this);


    }

    /**
     * 初始化子分类
     */

    public void loadListViewByNation(String string){
//        Toast.makeText(CategoryActivity.this,string,1000).show();
        personadapter=new personAdapter(this,string);
        mLisrView_right.setAdapter(personadapter);
        mLisrView_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(CategoryActivity.this,PersonDeatilActivity.class);
                intent.putExtra("headTitle", personText[i]);
                intent.putExtra("CityTitle", cityText[i]);
                intent.putExtra("YearTitle", YearText[i]);
                intent.putExtra("NumTitle", NumText[i]);
                startActivity(intent);
            }
        });
    }
    public void initChildCategories(int parentCategoryId) {
        getChildCategories(parentCategoryId);
    }


    public void addListener() {
        RelativeLayout searchView = (RelativeLayout) findViewById(R.id.category_search_input_view_id);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast(getResources().getString(R.string.search_default_text));
            }
        });
    }

    /**
     * 获取父分类
     */
    protected void getParentCategories() {
        if (NetWorkStateUtil.isNoConnected(context)) {
            toast(MsgConfig.NO_NETWORK_CONNECTION);
            return;
        }
        String url = buildParentCategoriesUrl();
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            renderParentCategoriesView(parseCategoriesJson((JSONObject) o));
                        } catch (Exception e) {
                            toast(MsgConfig.GET_ITEMS_FAILURE);
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast(MsgConfig.GET_ITEMS_FAILURE);
            }
        }
        ));
    }

    /**
     * 获取子分类
     */
    protected void getChildCategories(int parentCategoryId) {
        if (NetWorkStateUtil.isNoConnected(context)) {
            toast(MsgConfig.NO_NETWORK_CONNECTION);
            return;
        }
        String url = buildChildCategoriesUrl(parentCategoryId);
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            renderChildCategoriesView(parseCategoriesJson((JSONObject) o));
                        } catch (Exception e) {
                            toast(MsgConfig.GET_ITEMS_FAILURE);
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast(MsgConfig.GET_ITEMS_FAILURE);
            }
        }
        ));
    }


    /**
     * 用户点击父分类事件
     */
    private void parentCategoryOnClick() {
        parentCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastSelectedChildFragmentIndex = currentSelectedParentCategoryIndex;
                changeSelectCategoryView(i);
                if (parentItemDataObjectList.get(i) != null) {
                    CategoryDO categoryDO = (CategoryDO) parentItemDataObjectList.get(i).getData();
                    initChildCategories(categoryDO.getId());
                } else {
                    toast(MsgConfig.SYSTEM_ERROR);
                }
            }
        });
    }


    /**
     * 用户点击分类后，改变主分类列表样式
     *
     * @param position
     */
    private void changeSelectCategoryView(int position) {
        if (currentSelectedParentCategoryIndex == position) {
            return;
        } else {
            unSelectedParentCategoryItem(currentSelectedParentCategoryIndex);
        }
        ParentCategoryAdapter.selectCategoryIndex = position;
        selectParentCategoryItem(position);
    }

    /**
     * 取消上一次选中分类的边框颜色
     *
     * @param position
     */
    private void unSelectedParentCategoryItem(int position) {
        parentCategoryListView.setItemChecked(position, false);
        View view = parentCategoryListView.getViewByPosition(position);
        View leftIndicatorLine = view.findViewById(R.id.left_indicator_line);
        View rightIndicatorLine = view.findViewById(R.id.right_indicator_line);
        leftIndicatorLine.setVisibility(View.GONE);
        rightIndicatorLine.setVisibility(View.VISIBLE);
        RelativeLayout parentLayout = (RelativeLayout) view.findViewById(R.id.category_parent_btn_ly);
        parentLayout.setBackgroundColor(view.getResources().getColor(R.color.pinterest_backgroud));
    }

    /**
     * 设置本次选中的分类的边框颜色
     *
     * @param position 分类下标
     */
    private void selectParentCategoryItem(int position) {
        parentCategoryListView.setItemChecked(position, true);
        View view = parentCategoryListView.getViewByPosition(position);
        View leftIndicatorLine = view.findViewById(R.id.left_indicator_line);
        View rightIndicatorLine = view.findViewById(R.id.right_indicator_line);
        leftIndicatorLine.setVisibility(View.VISIBLE);
        rightIndicatorLine.setVisibility(View.GONE);
        RelativeLayout parentLayout = (RelativeLayout) view.findViewById(R.id.category_parent_btn_ly);
        parentLayout.setBackgroundColor(view.getResources().getColor(R.color.white));
        currentSelectedParentCategoryIndex = position;
    }


    /**
     * 生成 父分类的URL
     *
     * @return
     */
    private String buildParentCategoriesUrl() {
        StringBuilder path = new StringBuilder();
        try {
            String timstamp = String.valueOf(new Date().getTime());
            List<Parameter> parameters = new ArrayList<Parameter>();
            parameters.add(new Parameter(ApiConfig.SERVER_KEY_NAME, String.valueOf(AppConfig.SERVER_KEY)));
            parameters.add(new Parameter(ApiConfig.TIME_STAMP_NAME, timstamp));
            String token = AndroidSecretUtil.getToken(parameters, AppConfig.SERVER_SECRET);
            path.append(AppConfig.SERVER_DOMAIN);
            path.append(ApiConfig.GET_PARENT_CATEGORY);
            path.append("?").append(ApiConfig.SERVER_KEY_NAME).append("=").append(AppConfig.SERVER_KEY);
            path.append("&").append(ApiConfig.TIME_STAMP_NAME).append("=").append(timstamp);
            path.append("&").append(ApiConfig.SIGN_NAME).append("=").append(token);
        } catch (IOException e) {
            e.printStackTrace();
            toast(MsgConfig.SYSTEM_ERROR);
        }
        return path.toString();
    }

    /**
     * 生成 子分类的URL
     *
     * @return
     */
    private String buildChildCategoriesUrl(int parentCategoryId) {
        StringBuilder path = new StringBuilder();
        try {
            String timstamp = String.valueOf(new Date().getTime());
            List<Parameter> parameters = new ArrayList<Parameter>();
            parameters.add(new Parameter(ApiConfig.SERVER_KEY_NAME, String.valueOf(AppConfig.SERVER_KEY)));
            parameters.add(new Parameter(ApiConfig.TIME_STAMP_NAME, timstamp));
            parameters.add(new Parameter(ApiConfig.CATEGORY_ID, String.valueOf(parentCategoryId)));
            String token = AndroidSecretUtil.getToken(parameters, AppConfig.SERVER_SECRET);
            path.append(AppConfig.SERVER_DOMAIN);
            path.append(ApiConfig.GET_CHILD_CATEGORY);
            path.append("?").append(ApiConfig.SERVER_KEY_NAME).append("=").append(AppConfig.SERVER_KEY);
            path.append("&").append(ApiConfig.CATEGORY_ID).append("=").append(parentCategoryId);
            path.append("&").append(ApiConfig.TIME_STAMP_NAME).append("=").append(timstamp);
            path.append("&").append(ApiConfig.SIGN_NAME).append("=").append(token);
        } catch (IOException e) {
            e.printStackTrace();
            toast(MsgConfig.SYSTEM_ERROR);
        }
        return path.toString();
    }

    /**
     * 解析分类数据
     *
     * @param jsonObject
     */
    public ArrayList<ItemDataObject> parseCategoriesJson(JSONObject jsonObject) {
        ArrayList<ItemDataObject> itemDataObjectList = new ArrayList<ItemDataObject>();
        try {
            if (null != jsonObject && jsonObject.has("code") && jsonObject.getInt("code") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ItemDataObject itemDataObject = new ItemDataObject();
                    JSONObject json = jsonArray.getJSONObject(i);
                    CategoryDO categoryDO = new CategoryDO();

                    if (json.has("id") && !json.isNull("id")) {
                        categoryDO.setId(json.getInt("id"));
                    }
                    if (json.has("name") && !json.isNull("name")) {
                        categoryDO.setName(json.getString("name"));
                    }
                    if (json.has("father") && !json.isNull("father")) {
                        categoryDO.setFather(json.getInt("father"));
                    }
                    if (json.has("pic") && !json.isNull("pic")) {
                        categoryDO.setPic(json.getString("pic"));
                    }
                    if (json.has("sequence") && !json.isNull("sequence")) {
                        categoryDO.setSequence(json.getInt("sequence"));
                    }
                    if (json.has("gmtCreated") && !json.isNull("gmtCreated")) {
                        categoryDO.setGmtCreated(json.getString("gmtCreated"));
                    }
                    if (json.has("gmtModified") && !json.isNull("gmtModified")) {
                        categoryDO.setGmtModified(json.getString("gmtModified"));
                    }
                    itemDataObject.setData(categoryDO);
                    itemDataObjectList.add(itemDataObject);
                }
            } else {
                toast(MsgConfig.GET_CATEGORY_FAILURE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemDataObjectList;
    }

    /**
     * 渲染结果页面
     */
    public void renderParentCategoriesView(List<ItemDataObject> itemDataObjectList) {
        //渲染右侧默认的分类列表
        parentItemDataObjectList = itemDataObjectList;
        if (itemDataObjectList != null && itemDataObjectList.size() > ParentCategoryAdapter.selectCategoryIndex) {
            ItemDataObject itemDataObject = itemDataObjectList.get(ParentCategoryAdapter.selectCategoryIndex);
            if (itemDataObject.getData() != null) {
                CategoryDO categoryDO = (CategoryDO) itemDataObject.getData();
                int categoryId = categoryDO.getId();
                initChildCategories(categoryId);
            }

        }
        //渲染左侧主分类列表
        parentCategoryAdapter.addCategory(itemDataObjectList);
        parentCategoryAdapter.notifyDataSetChanged();
    }

    //利用volley获取server下发的json
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
                        ParseJsonMulti(response.toString(),"nation","nationlist",shifu1);
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

    //解析JSON数组串
    private void ParseJsonMulti(String strResult,String mJsonObject,String mJsonArrayName,String shifu) {
        try {
            JSONObject jsonObjs = new JSONObject(strResult).getJSONObject(mJsonObject);
            JSONArray jsonArray = jsonObjs.getJSONArray(mJsonArrayName);
//            for (int i = 0; i < jsonObjs.length(); i++) {
//                JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
//
//               mTextView.setText(jsonObject2.getString("name"));
//            }
            JSONObject jsonObject_Name2 = (JSONObject)jsonArray.opt(0);
            test_tv.setText(jsonObject_Name2.getString("name"));

        } catch (JSONException e) {
            System.out.println("Jsons Parse Error !");
            e.printStackTrace();
        }
    }

    /**
     * 渲染子分类列表
     *
     * @param itemDataObjectList
     */
    public void renderChildCategoriesView(ArrayList<ItemDataObject> itemDataObjectList) {
        if (childFragmentMap.containsKey(currentSelectedParentCategoryIndex)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(childFragmentMap.get(lastSelectedChildFragmentIndex)).show(childFragmentMap.get(currentSelectedParentCategoryIndex)).commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ChildCategoryFragment.CHILD_CATEGORIES_TAG, itemDataObjectList);
            ChildCategoryFragment childCategoryFragment = ChildCategoryFragment.newInstance(bundle);
            childFragmentMap.put(currentSelectedParentCategoryIndex, childCategoryFragment);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (currentSelectedParentCategoryIndex == 0 && fragmentTransaction.isEmpty()) {
                fragmentTransaction.add(R.id.child_category_content, childCategoryFragment).commit();
            } else {
                fragmentTransaction.hide(childFragmentMap.get(lastSelectedChildFragmentIndex)).add(R.id.child_category_content, childCategoryFragment).commit();
            }

        }

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

    public class personAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private List<Map<String,Object>> mData;
        private String string;

        public personAdapter(Context context,String string){
            mInflater=LayoutInflater.from(context);
            this.string=string;
            Log.e("TAG",string);
            init();
        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
        //convertView为null的时候初始化convertView
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.mlist_view, null);
            holder.img = (ImageView)view.findViewById(R.id.listview_iv);
            holder.title = (TextView)view.findViewById(R.id.listview_tv);
            holder.City = (TextView)view.findViewById(R.id.listview_city);
            holder.Year = (TextView)view.findViewById(R.id.listview_year);
            holder.Num = (TextView)view.findViewById(R.id.listview_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.img.setBackgroundResource((Integer)mData.get(i).get("img"));
        holder.title.setText(mData.get(i).get("title").toString());
            holder.City.setText(mData.get(i).get("city").toString());
            holder.Year.setText(mData.get(i).get("year").toString());
            holder.Num.setText(mData.get(i).get("num").toString());
        return view;
    }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void init(){
            mData=new ArrayList<Map<String, Object>>();
            for(int i=0;i<3;i++){
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("title",personText[i]);
                map.put("year",YearText[i]);
                map.put("num",NumText[i]);
                map.put("city",cityText[i]);
                map.put("img", mDrawable[i]);
                mData.add(map);
            }
        }
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView City;
        public TextView Year;
        public TextView Num;
    }
}
