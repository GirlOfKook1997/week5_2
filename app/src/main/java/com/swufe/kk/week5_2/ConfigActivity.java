package com.swufe.kk.week5_2;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

public class ConfigActivity extends AppCompatActivity {
    public final String TAG="ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent=getIntent();
        //得到从RateActivity中传输过来的参数值
        float dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        float won2=intent.getFloatExtra("won_rate_key",0.0f);
        //日志显示是否得到了数据
        Log.i(TAG,"onCreate:dollar2="+dollar2);
        Log.i(TAG,"onCreate:euror2="+euro2);
        Log.i(TAG,"onCreate:won2="+won2);
        //找到configActivity中的三个控件
        dollarText=findViewById(R.id.dollar_rate);
        euroText=findViewById(R.id.euro_rate);
        wonText=findViewById(R.id.won_rate);

        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }
    public void save(View btn){
        Log.i(TAG,"save: ");
        //获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());//getText toString可能出现异常
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon= Float.parseFloat(wonText.getText().toString());

        Log.i(TAG,"save:获取到新的值");
        Log.i(TAG,"save:dollar2="+newDollar);
        Log.i(TAG,"save:euro2="+newEuro);
        Log.i(TAG,"save:won2="+newWon);
        //保存到Bundle或者放入到Extra
        Intent intent=getIntent();
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);//2 响应代码，通过它使带回的数据究竟按什么格式拆分

        //返回到调用页面
        finish();//结束当前页面，回到原来的页面
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
