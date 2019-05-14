package com.swufe.kk.week5_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<String> data=new ArrayList<String>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        //GridView可以确定多列
        ListView listview=findViewById(R.id.mylist);
       // String data[]={"刘昊然","田柾国","黄旭熙"};
        for(int i=0;i<10;i++)
        {
            data.add("item"+i);
        }
        adapter=new ArrayAdapter <String>(this,android.R.layout.simple_list_item_1,data);
        //没有数据才会显示
        listview.setAdapter(adapter);
        listview.setEmptyView(findViewById(R.id.nodata));
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        adapter.remove(listv.getItemAtPosition(position));//仅仅限于ArrayAdapter
        //adapter.notifyDataSetChanged();//自动刷新
    }
}
