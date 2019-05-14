package com.swufe.kk.week5_2;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private String TAG="mylist2";
    Handler handler;
    private List<HashMap<String,String>> listItems; //存放文字或者图片等其他信息
    private SimpleAdapter listItemAdapter; //适配器，用于将表中显示内容和Map中的k,v对应起来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
       // MyAdapter myAdapter=new MyAdapter(this,R.layout.list_item,listItems);
       // this.setListAdapter(myAdapter);
        this.setListAdapter(listItemAdapter);
        Thread t=new Thread(this);
        t.start();
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==7)
                {
                    listItems= (List<HashMap<String,String>>) msg.obj;
                    listItemAdapter =new SimpleAdapter(MyList2Activity.this,listItems,
                            R.layout.list_item,//用于显示一行数据的显示布局
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    private void initListView()
    {
       listItems=new ArrayList<HashMap<String,String>>();
       for(int i=0;i<10;i++){
           //一个map可以由很多k,v；因此可以增加put来增加数据项
           HashMap<String,String> map=new HashMap<String,String>();
           map.put("ItemTitle","Rate: "+i);//标题文字
           map.put("ItemDetail","detail"+i);//详情描述
           //Map中放Key的名字是不能重复的
           listItems.add(map);
       }
       //生成适配器的Item和动态数组对应的元素
        listItemAdapter =new SimpleAdapter(this,listItems,//数据源
                R.layout.list_item,//用于显示一行数据的显示布局
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
        );
    }

    @Override
    public void run() {
        List<HashMap<String,String>> retList=new ArrayList<HashMap<String, String>>();
        Document doc = null;
        try {
            //Thread.sleep(1000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc=Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables=doc.getElementsByTag("table");
            Element table1=tables.get(1);
            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8)
            {
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:text="+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
       // catch (InterruptedException e) {
         //   e.printStackTrace();
       // }
        Message msg=handler.obtainMessage(7);
        msg.obj=retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"onItemClick:parent="+parent);
        Log.i(TAG,"onItemClick:view="+view);
        Log.i(TAG,"onItemClick:position="+position);
        Log.i(TAG,"onItemClick:id="+id);
        HashMap<String,String> map=(HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");
        Log.i(TAG,"onItemClick:titleStr="+titleStr);
        Log.i(TAG,"onItemClick:detailStr="+detailStr);
        //无论是从map中获取数据还是从view中获取数据都是一样的

        //打开新的页面，传入新的参数
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        Log.i(TAG,"onItemClick:titleStr="+titleStr);
        Log.i(TAG,"onItemClick:detailStr="+Float.parseFloat(detailStr));
        startActivity(rateCalc);

    }
   //长按结束后，同时会触发短按的按钮
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG,"onItemClick ：position="+position);
        //删除操作
        //。。。
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //先改数据，再通知Adapter刷新

        //构造对话框进行确认操作
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { //which指哪个按钮响应
                Log.i(TAG,"onClick:对话框事件处理");
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        Log.i(TAG,"onItenLongClick:size="+listItems.size());
        return true;//真或假取决于是否屏蔽掉短按事件
    }
}
