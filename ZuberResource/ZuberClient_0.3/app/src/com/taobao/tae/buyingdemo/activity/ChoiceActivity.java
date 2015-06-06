package com.taobao.tae.buyingdemo.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.taobao.tae.buyingdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChoiceActivity extends TabActivity
{
    //声明TabHost对象
    TabHost mTabHost;
    private int[] mDrawable={R.drawable.image2,R.drawable.search_icon,R.drawable.search_icon};
    private String[] personText={"wangshifu","zhangshifu","sunshifu"};
    private int[] images_1={R.drawable.zubai1,R.drawable.zubao2,R.drawable.zubao3,R.drawable.zubao4,R.drawable.zubao5,R.drawable.zubao6};
    private String[] texts_1={"1","2","3","4","5","6"};
    private int[] images_2={R.drawable.cailiao1,R.drawable.cailiao2,R.drawable.cailiao3,R.drawable.cailiao4,R.drawable.cailiao5,R.drawable.cailiao6};
    private String[] texts_2={"1","2","3","4","5","6"};
    private int[] images_3={R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon,R.drawable.app_icon};
    private String[] texts_3={"1","2","3","4","5","6"};
    private personAdapter personadapter;
    private GridView listview_1;
    private GridView listview_2;
    private GridView listview_3;
    private RelativeLayout mPopupwindowRL;
    private String currentZubao;
    private String currentCailiao;
    private String currentHuawen;
    private View currentZubaoView;
    private View currentCailiaoView;
    private View currentHuawenView;
    private boolean zuBaoState=false;
    private boolean CailiaoState=false;
    private boolean HuawenState=false;
    private PopupWindow mPopUpWindow;
    private TextView pop_tv_1;
    private TextView pop_tv_2;
    private TextView pop_tv_3;

    private ImageView pop_close;
    private Button pop_buy;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_choice_layout);
        listview_1= (GridView) findViewById(R.id.choice_listview_1);
        listview_2= (GridView) findViewById(R.id.choice_listview_2);
        listview_3= (GridView) findViewById(R.id.choice_listview_3);
        ArrayList<HashMap<String,Object>> mDate=new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<6;i++){
            HashMap<String,Object> map=new HashMap<String, Object>();
            map.put("itemImage",images_2[i]);
            map.put("itemTexts",texts_2[i]);
            mDate.add(map);
        }
        SimpleAdapter mAdapter=new SimpleAdapter(this,mDate,
                R.layout.mygridview_layout,new String[]{"itemImage","itemTexts"},
                new int[]{R.id.itemImage,R.id.itemText});
        listview_1.setAdapter(mAdapter);
        listview_2.setAdapter(mAdapter);
        listview_3.setAdapter(mAdapter);

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(CailiaoState!=true||HuawenState!=true){
                    Toast.makeText(ChoiceActivity.this,"select:"+texts_1[i],Toast.LENGTH_SHORT).show();
                    currentZubao=texts_1[i];
                    zuBaoState=true;
                    currentZubaoView=view;
                    currentZubaoView.setBackgroundColor(Color.rgb(255,255,255));
                }
                else{
                    currentZubao=texts_1[i];
//                    Toast.makeText(ChoiceActivity.this,"open",Toast.LENGTH_SHORT).show();
                    initPopUpwindow();
                }
            }
        });
        listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(zuBaoState!=true||HuawenState!=true){
                    Toast.makeText(ChoiceActivity.this,"select:"+texts_1[i],Toast.LENGTH_SHORT).show();
                    currentCailiao=texts_1[i];
                    CailiaoState=true;
                    view.setBackgroundColor(Color.rgb(255,255,255));
                }
                else{
                    currentCailiao=texts_1[i];
//                    Toast.makeText(ChoiceActivity.this,"open",Toast.LENGTH_SHORT).show();
                    initPopUpwindow();
                }
            }
        });
        listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(CailiaoState!=true||CailiaoState!=true){
                    Toast.makeText(ChoiceActivity.this,"select:"+texts_1[i],Toast.LENGTH_SHORT).show();
                    currentHuawen=texts_1[i];
                    HuawenState=true;
                    view.setBackgroundColor(Color.rgb(255,255,255));
                }
                else{
                    currentHuawen=texts_1[i];
//                    Toast.makeText(ChoiceActivity.this,"open",Toast.LENGTH_SHORT).show();
                    initPopUpwindow();
                }
            }
        });

        //取得TabHost对象
        mTabHost = getTabHost();


        //新建一个newTabSpec(newTabSpec)
        //设置其标签和图标(setIndicator)
        //设置内容(setContent)
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
                .setIndicator("珠宝")
                .setContent(R.id.choice_listview_1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
                .setIndicator("材料")
                .setContent(R.id.choice_listview_2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
                .setIndicator("花纹")
                .setContent(R.id.choice_listview_3));

        //设置TabHost的背景颜色
//        mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
        //设置TabHost的背景图片资源
        //mTabHost.setBackgroundResource(R.drawable.bg0);

        //设置当前显示哪一个标签
        mTabHost.setCurrentTab(0);

        //标签切换事件处理，setOnTabChangedListener
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            // TODO Auto-generated method stub
            @Override
            public void onTabChanged(String tabId)
            {

            }
        });
    }

    private void initPopUpwindow() {
        View view=LayoutInflater.from(this).inflate(R.layout.popupwindow_forall,null);
        pop_tv_1= (TextView) view.findViewById(R.id.pop_tv_1);
        pop_tv_2= (TextView) view.findViewById(R.id.pop_tv_2);
        pop_tv_3= (TextView) view.findViewById(R.id.pop_tv_3);
        pop_close= (ImageView) view.findViewById(R.id.pop_close);
        pop_buy= (Button) view.findViewById(R.id.pop_buy);
        pop_tv_1.setText(currentZubao);
        pop_tv_2.setText(currentCailiao);
        pop_tv_3.setText(currentHuawen);
        mPopUpWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                800,true);
        mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopUpWindow.setOutsideTouchable(true);
        mPopUpWindow.setFocusable(true);
        pop_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopUpWindow.dismiss();
            }
        });
        pop_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChoiceActivity.this,MainActivity.class);
                intent.putExtra("extra","popbuy");
                startActivity(intent);
                MainActivity.isFromChoice=true;
            }
        });
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.4f;
        getWindow().setAttributes(lp);
        mPopUpWindow.update();
        mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().setAttributes(lp);
            }
        });
        mPopUpWindow.showAtLocation(ChoiceActivity.this.findViewById(R.id.choice_layout),Gravity.BOTTOM,0,0);
        Log.e("TAG","popupwindow");
    }

    public class personAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<Map<String,Object>> mData;
        private String string;

        public personAdapter(Context context,String string){
            mInflater=LayoutInflater.from(context);
            this.string=string;
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
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            holder.img.setBackgroundResource((Integer)mData.get(i).get("img"));
            holder.title.setText(mData.get(i).get("title").toString());
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
                map.put("title",personText[i]+string);
                map.put("img",mDrawable[i]);
                mData.add(map);
            }
        }
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView title;
    }
}