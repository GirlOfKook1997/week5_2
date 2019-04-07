package com.swufe.kk.week5_2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        show=findViewById(R.id.showOut);

    }
    public void onClick(View btn) {
          //获取用户输入内容
        String str=rmb.getText().toString();
        //防止用户输入为空的情况出现，所以将r设为全局变量，使其默认值为0
        float r=0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else {
            //提示用户输入内容
            Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show();
                           //根据目前activity显示，提示内容，显示时间的长短
        }
        float val=0;
           if(btn.getId()==R.id.btn_dollar)
           {
               //6.7属于double类型的数据：需要的是float类型，但实际计算式double类型，因此需要强制转换
               val=(float)(Math.round((r *(1/6.7f))*100))/100;
           }else if(btn.getId()==R.id.btn_euro)
           {
               //val是局部变量，适用范围是在同一个判断框内，所以可以重名
               val=(float)(Math.round((r*(1/11f))*100))/100; //1/11仍然是一个整数,需要改变类型
           }else if(btn.getId()==R.id.btn_won)
           {
               val=(float)(Math.round((r*500)*100))/100;
           }
        //输出类型规定为string类型，另一中转换方法是(val+"")
        show.setText(String.valueOf(val));

    }
}
