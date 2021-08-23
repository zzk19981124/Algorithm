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
import android.renderscript.Sampler;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST = 1;
    private static final String TAG = "MainActivity-------->";
    private Button getDataBtn, translationBtn,btn3;
    private ScrollView mainLayout;
    private ArrayList<GeoHelper.Pt> csvLists = new ArrayList<>();
    private CsvUtil csvUtil;
    private TextView showData;

    ArrayList<GeoHelper.Pt> get = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> beforeCSV = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> afterCSV = new ArrayList<>();
    private static final double lonMi = 0.00001141; //经度每移动1米度数变化
    private static final double latMi = 0.00000899;//维度每移动1米度数变化
    private static final double CarWidth = 3;//车宽设置为3m
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        //android11 新特性，需要强制启用
        //XXPermissions.setScopedStorage(true);
        setContentView(R.layout.activity_main);
        initBind();//初始化控件

        //平移曲线
        //translationCurve(afterCSV, 1, 10);
        //平移曲线，使用局部坐标系
        //translationNEU(afterCSV, 1, 10);
    }

    //平移曲线，使用局部坐标系,enu东北天
    private ArrayList<GeoHelper.Pt> translationNEU(ArrayList<GeoHelper.Pt> lineCSV,
                                                   int direction, double distance) {
        ArrayList<GeoHelper.Pt> originalData = new ArrayList<>();
        originalData = lineCSV;
        switch (direction){
            case 1://北
                for (GeoHelper.Pt data : originalData) {
                    data.y+=10;
                }
                break;
            case 2://南
                for (GeoHelper.Pt data : originalData) {
                    data.y-=10;
                }
                break;
            case 3://西
                for (GeoHelper.Pt data : originalData) {
                    data.x-=10;
                }
                break;
            case 4://东
                for (GeoHelper.Pt data : originalData) {
                    data.x+=10;
                }
                break;
            default:
                break;
        }
        return originalData;
    }

    //实现平移曲线的函数 , 参数：数据集、方向、平移的米数
    private ArrayList<GeoHelper.Pt> translationCurve(ArrayList<GeoHelper.Pt> lineCSV,
                                                     int direction, double distance) {
        ArrayList<GeoHelper.Pt> originalData = new ArrayList<>();
        originalData = CsvUtil.getJw();
        switch (direction) {
            case 1://北,纬度+
                for (GeoHelper.Pt data : originalData) {
                    data.x = data.x + distance * latMi;
                }
                break;
            case 2://南，纬度-
                for (GeoHelper.Pt data : originalData) {
                    data.x = data.x - distance * latMi;
                }
                break;
            case 3://西，经度-，默认东经
                for (GeoHelper.Pt data : originalData) {
                    data.y = data.x - distance * lonMi;
                }
                break;
            case 4://东，经度+，默认东经
                for (GeoHelper.Pt data : originalData) {
                    data.y = data.x + distance * lonMi;
                }
                break;
            default:
                break;
        }
        return originalData;
    }

    //初始化控件以及设置文本框可复制粘贴
    private void initBind() {

        mainLayout = findViewById(R.id.main_layout);
        translationBtn = findViewById(R.id.btn_translation);
        getDataBtn = findViewById(R.id.btn_get_data);
        btn3 = findViewById(R.id.btn_pingyi_c);
        //kBtnData.setVisibility(View.GONE);//第二个按钮设置为不可见
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
        getDataBtn.setOnClickListener(this);
        translationBtn.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_translation://向北平移10米
                ArrayList<GeoHelper.Pt> enuData = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                enuData = translationNEU(afterCSV,1,3);
                for (GeoHelper.Pt data : enuData) {
                    //String string = String.valueOf(data.x).concat("," + String.valueOf(data.y).concat("," + String.valueOf(data.z)) + "\n");
                    String string = String.valueOf(data.x).concat("," + String.valueOf(data.y)) + "\n";
                    sb.append(string);
                }
                showData.setText(sb);
                break;
            case R.id.btn_get_data:
                showData.setText(fromCSV());//从csv文件读取
                break;
            case R.id.btn_pingyi_c:
                //通过曲率计算平移曲线
                Toast.makeText(this, "111111", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showData.setText(fromCurvature());
                    }
                });

                break;
            default:
                break;
        }
    }

    //将csv文件解析到文本框当中
    private StringBuilder fromCSV() {

        beforeCSV = csvUtil.fetch_csv("refPoses.csv");
        for (int i = 0;i<5;i++){
            Log.d(TAG, "beforeCSV打印输出前5行 ---->: "+beforeCSV.get(i).x+","+beforeCSV.get(i).y+","+beforeCSV.get(i).z);
        }
        Log.i(TAG, "beforeCSV的长度: " + beforeCSV.size());
        if (beforeCSV.size() == 0) return null;
        afterCSV = LTTB.getLTTB(beforeCSV, beforeCSV.size() / 10);//使用过滤算法，点数降为1/3
        for (int i = 0;i<5;i++){
            Log.d(TAG, "afterCSV打印输出前5行 ---->: "+afterCSV.get(i).x+","+afterCSV.get(i).y+","+afterCSV.get(i).z);
        }
        Log.i(TAG, "fromCSV: " + afterCSV.size());
        StringBuilder sb = new StringBuilder();
        for (GeoHelper.Pt data : afterCSV) {
            //String string = String.valueOf(data.x).concat("," + String.valueOf(data.y).concat("," + String.valueOf(data.z)) + "\n");
            String string = String.valueOf(data.x).concat("," + data.y + "\n");
            sb.append(string);
        }
        return sb;
    }
    /**
     * 将得到的曲率对应的平移曲线计算出来
     * */
    private StringBuilder fromCurvature(){
        StringBuilder sb = new StringBuilder();
        beforeCSV = csvUtil.fetch_csv("refPoses.csv");
        afterCSV = LTTB.getLTTB(beforeCSV, beforeCSV.size() / 3);//使用过滤算法，点数降为1/3
        Log.i(TAG, "fromCurvature " + afterCSV.size());
        System.out.println(" fromCurvature zhe    "+afterCSV.get(0));
        //得到该数据集中每个点对应的曲率
        //List<Double> getCurvature = CsvUtil.countCurvature(afterCSV);
        List<double[]> getVector = CsvUtil.countNormK(afterCSV);  //得到矢量点的集合

        //打印 getVector 输出
        for (int i =0;i<10;i++){
            Log.d(TAG, "fromCurvature: ---->" + getVector.get(i)[0]+"    "+getVector.get(i)[1]);
        }

        List<double[]> list = new ArrayList<>(getVector.size());
        for (int i =0;i<getVector.size();i++){
            double[] l = new double[2];
            for (int j = 0;j<2;j++){
                l[j] = getVector.get(i)[j];  // 车宽在这里即是圆的半径 ， 矢量*半径得到圆心画过的轨迹
            }
            list.add(l);
        }
        for (double[] qq:list){
            String string = String.valueOf(qq[0]).concat(","+qq[1]) + "\n";
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