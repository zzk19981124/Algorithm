package com.example.algorithm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algorithm.Helper.GeoHelper;
import com.example.algorithm.Helper.MyDBOpenHelper;
import com.example.algorithm.lttb.LTTB;
import com.example.algorithm.utils.CsvUtil;
import com.example.algorithm.utils.WriteFileUil;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST = 1;
    private static final String TAG = "MainActivity-------->";
    private Button kBtnData, createDbBtn;
    private ScrollView mainLayout;
    private ArrayList<GeoHelper.Pt> csvLists = new ArrayList<>();
    private CsvUtil csvUtil;
    private TextView showData;

    ArrayList<GeoHelper.Pt> get = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> beforeCSV = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> afterCSV = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBind();//初始化控件
    }
    //初始化控件以及设置文本框可复制粘贴
    private void initBind() {
        mainLayout = findViewById(R.id.main_layout);
        kBtnData = findViewById(R.id.btn_create_db);
        createDbBtn = findViewById(R.id.btn_getPermission);
        kBtnData.setVisibility(View.GONE);//第二个按钮设置为不可见
        showData = findViewById(R.id.reducedData);
        showData.setMovementMethod(ScrollingMovementMethod.getInstance());
        int _sdkLevel = Build.VERSION.SDK_INT;
        if (_sdkLevel >= 11) {
            showData.setTextIsSelectable(true);
        } else {
            showData.setFocusableInTouchMode(true);
            showData.setFocusable(true);
            showData.setClickable(true);
            showData.setLongClickable(true);
            showData.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            showData.setText(showData.getText(), TextView.BufferType.SPANNABLE);
        }
        csvUtil = new CsvUtil(this);
        kBtnData.setOnClickListener(this);
        createDbBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_db:
                break;
            case R.id.btn_getPermission:
                showData.setText(fromCSV());//从csv文件读取
                break;
        }
    }

    //将csv文件解析到文本框当中
    private StringBuilder fromCSV(){
        beforeCSV = csvUtil.fetch_csv2("1.csv");
        Log.i(TAG, "fromCSV: "+beforeCSV.size());
        if (beforeCSV.size()==0) return null;
        afterCSV = LTTB.getLTTB(beforeCSV,beforeCSV.size()/10);//使用过滤算法，点数降为1/3
        Log.i(TAG, "fromCSV: "+afterCSV.size());
        StringBuilder sb = new StringBuilder();
        for (GeoHelper.Pt data:afterCSV){
            String string = String.valueOf(data.x).concat(","+String.valueOf(data.y).concat(","+String.valueOf(data.z))+"\n");
            sb.append(string);
        }
        return sb;
    }
    //检查权限，如果没有则跳转到权限申请页面
    private void checkPermission() {
        List<String> mPermissionList = new ArrayList<>();
        String[] permissions = {Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        //mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                mPermissionList.add(permission);
        }
        //判断是否为空
        if (!mPermissionList.isEmpty()) {
            String[] permissionss = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissionss, PERMISSION_REQUEST);
        }
    }

    public void toast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}