package com.swufe.kk.week5_2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.log;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate="";
    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);

        //获取SP里面保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);//myrate为文件名，是用于存放少量数据
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);//myrate不能改,智能获得一个配置文件
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate=sharedPreferences.getString("update_date","");
        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);


        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);
        Log.i(TAG,"onCreate:sp updateDate="+updateDate);

        if(!todayStr.equals(updateDate)){
            //开启子线程
            Thread t=new Thread(this);
            t.start();//给命令再执行，否则就等待
        }else{
            Log.i(TAG,"onCreate:不需要更新");
        }

         handler=new Handler()    //用于保存并且处理带回的数据
        {
            @Override
            public void handleMessage(Message msg) {

                if(msg.what==5)
                {
                   Bundle bdl=(Bundle)msg.obj;
                   dollarRate=bdl.getFloat("dollar-rate");
                   euroRate=bdl.getFloat("euro-rate");
                   wonRate=bdl.getFloat("won-rate");
                    Log.i(TAG,"handlerMessage:dollarRate="+dollarRate);
                    Log.i(TAG,"handlerMessage:euroRate="+euroRate);
                    Log.i(TAG,"handlerMessage:wonRate="+wonRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);//myrate为文件名，是用于存放少量数据
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.apply();

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        }; //用于子线程和主线程之间的数据交换


    }
    public void openOne(View btn)
  {
      openConfig();
    }

    public void onClick(View btn) {
        //获取用户输入内容
        String str = rmb.getText().toString();
        //防止用户输入为空的情况出现，所以将r设为全局变量，使其默认值为0
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            //提示用户输入内容
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            //根据目前activity显示，提示内容，显示时间的长短
        }
        float val = 0;
        if (btn.getId() == R.id.btn_dollar) {
            //6.7属于double类型的数据：需要的是float类型，但实际计算式double类型，因此需要强制转换
            //val=(float)(Math.round((r *(1/6.7f))*100))/100;
            show.setText(String.format("%.2f", r * dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            //val是局部变量，适用范围是在同一个判断框内，所以可以重名
            val = (float) (Math.round((r * (1 / 11f)) * 100)) / 100; //1/11仍然是一个整数,需要改变类型
            show.setText(String.format("%.2f", r * euroRate));
        } else if (btn.getId() == R.id.btn_won) {
            //val=(float)(Math.round((r*500)*100))/100;
            // val=String.format("%.2f",r*wonRate);
            show.setText(String.format("%.2f", r * wonRate));
        }
        //输出类型规定为string类型，另一中转换方法是(val+"")
        // show.setText(String.valueOf(val));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);

        return true;
    } //创建菜单

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口t
            Intent list=new Intent(this,MyList2Activity.class);

            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Intent config=new Intent(this,ConfigActivity.class); //常常使用intent传输数据到另外页面
        config.putExtra("dollar_rate_key",dollarRate); //前面是key，后面是值
        config.putExtra("euro_rate_key",euroRate);  //putExtra方法是把数据装进intent当中
        config.putExtra("won_rate_key",wonRate);
        //另一种方法是用bundle封装数据进行传输

        Log.i(TAG,"openOne: dollarRate="+dollarRate); //日志文件检查输入是否成功
        Log.i(TAG,"openOne: euroRate="+euroRate);
        Log.i(TAG,"openOne: wonRate="+wonRate);
        //startActivity(config); //跳转页面
        startActivityForResult(config,1);//打开配置文件页面是为了带回数据
        // 通过1区分是谁返回的数据，如按钮1 还是 按钮2
    }
    @Override
    //request code确定那个按钮返回的数据
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate =bundle.getFloat("key_dollar",0.1f);
            euroRate =bundle.getFloat("key_euro",0.1f);
            wonRate =bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActivityResult:wonRate="+wonRate);
            //dollarRate是全局变量，所以改变后下次使用时就是新的值

            //将数据保存在sp中
            SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();//apply()方法也可以执行相同的功能，但耗时较长
            Log.i(TAG,"onActivityResult:数据已保存到sp");
        }
    }

    @Override
    public void run() {
        Log.i(TAG,"run:");
            try {
                Thread.sleep(3000); //每一个获取之间间隔2000ms是为了使程序运行速度不要那么快
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //用于保存获取的汇率
        Bundle bundle;


           //获取网络数据
            /*URL url=null;
            try {
                url=new URL("http://www.usd-cny.com/bankofchina.htm");
                HttpURLConnection http=(HttpURLConnection)url.openConnection();
                InputStream in=http.getInputStream();
                String html=inputStream2String(in);
                Document doc=Jsoup.parse(html);//把源文件交给Jsoup
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }*/
        bundle=getFromBOC();
            //bundle中所获取的数据进行带回
        //获取Msg对象，用于返回给主线程
        Message msg=handler.obtainMessage(5); //用与改变msg里面的内容
        // msg.what=5;//用于标记msg对象的属性，就比如一个人登记信息时，用电话号码对人进行标记
        msg.obj=bundle;
        handler.sendMessage(msg);

            }

    /**
     * 从bankofchina获取数据
     * @return
     */

    private Bundle getFromBOC() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc=Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables=doc.getElementsByTag("table");
            int a=1;
            /*for(Element table:tables){
                Log.i(TAG,"run:table["+a+"]="+table);
                a++;
            }*/
            Element table1=tables.get(1);
           // Log.i(TAG,"run:table1="+table1);
            //获取TD中的数据
            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8)
            {
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:text="+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }

            for(Element td:tds){
                Log.i(TAG,"run:td="+td);
                td.text();//text与html相似，但是text与html中间仍然有差别
            }

            //Elements newsHeadlines = doc.select("#mp-itn b a");
            //for (Element headline : newsHeadlines) {
              //  Log.i(TAG,"%s\n\t%s"+headline.attr("title")+ headline.absUrl("href"));
            //}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private Bundle getFromUsdCny() {
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc=Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables=doc.getElementsByTag("table");
            int a=1;
            /*for(Element table:tables){
                Log.i(TAG,"run:table["+a+"]="+table);
                a++;
            }*/
            Element table1=tables.get(0);
            // Log.i(TAG,"run:table1="+table1);
            //获取TD中的数据
            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6)
            {
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:text="+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }

            for(Element td:tds){
                Log.i(TAG,"run:td="+td);
                td.text();//text与html相似，但是text与html中间仍然有差别
            }

            //Elements newsHeadlines = doc.select("#mp-itn b a");
            //for (Element headline : newsHeadlines) {
            //  Log.i(TAG,"%s\n\t%s"+headline.attr("title")+ headline.absUrl("href"));
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
       }
    }

