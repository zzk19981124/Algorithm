package com.example.algorithm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.algorithm.utils.CsvUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btn;
    private List<String[]> csvLists = new ArrayList<>();
    private CsvUtil csvUtil;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    //csvLists = csvUtil.fetch_csv("convert.csv");
                    //System.out.println("------------csv有"+csvLists.size()+"组数据----------------");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBind();

        //csvLists = csvUtil.fetch_csv("convert.csv");
        csvLists = csvUtil.fetch_csv("convert.csv");
        System.out.println("------------csv有"+csvLists.size()+"组数据----------------");
        if (csvLists!=null){
            for (int i =0;i<csvLists.size();i++){
                for (int j = 0;j<2;j++){
                    System.out.println(csvLists.get(i)[j]);
                }
            }
        }
    }

    private void initBind() {
        btn = findViewById(R.id.btn);
        csvUtil = new CsvUtil(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        });
    }
}