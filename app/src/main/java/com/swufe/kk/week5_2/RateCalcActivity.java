package com.swufe.kk.week5_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalcActivity extends AppCompatActivity {

    float rate=0f;
    EditText inp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        rate=getIntent().getFloatExtra("rate",0f);
        String title=getIntent().getStringExtra("title");
        ((TextView)findViewById(R.id.title2)).setText(title);
        inp2=findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             TextView show=findViewById(R.id.show2);
             if(s.length()>0)
             {
                 float val=Float.parseFloat(s.toString());
                 show.setText(val+"RMB==>"+(100/rate*val));
             }else{
                 show.setText("");
             }
            }
        });

    }
}
