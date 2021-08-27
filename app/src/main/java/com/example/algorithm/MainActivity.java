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
    private Button getDataBtn, translationBtn, btn3;
    private TextView showData;
    private ArrayList<GeoHelper.Pt> beforeCSV = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> afterCSV = new ArrayList<>();
    private static final double CarWidth = 3;//车宽设置为3m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();//检查并申请权限
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBind();//初始化控件
        beforeCSV = CsvUtil.cutDataNoToENU("path.csv",this);
        Log.i(TAG, "beforeCSV的长度: " + beforeCSV.size());
        afterCSV = LTTB.getLTTB(beforeCSV, beforeCSV.size() / 3);//使用过滤算法，点数降为1/3
        Log.i(TAG, "fromCSV: " + afterCSV.size());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_translation://向北平移10米
                StringBuilder sb = new StringBuilder();
                ArrayList<GeoHelper.Pt> enuData = CsvUtil.translationNEU(afterCSV, 4, 3);
                for (GeoHelper.Pt data : enuData) {
                    String string = String.valueOf(data.x).concat("," + data.y) + "\n";
                    sb.append(string);
                }
                showData.setText(sb);
                break;
            case R.id.btn_get_data:
                showData.setText(fromCSV());//从csv文件读取
                break;
            case R.id.btn_pingyi_c:
                //通过曲率计算平移曲线
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showData.setText(printToTV(fromCurvature(beforeCSV)));
                    }
                });
                break;
            default:
                break;
        }
    }

    //将csv文件解析到文本框当中
    private StringBuilder fromCSV() {
        StringBuilder sb = new StringBuilder();
        for (GeoHelper.Pt data : afterCSV) {
            String string = String.valueOf(data.x).concat("," + data.y + "\n");
            sb.append(string);
        }
        return sb;
    }

    /**
     * 将得到的曲率对应的平移曲线计算出来
     * <p>
     * i+1  -i  得到速度矢量
     */
    private List<double[]> fromCurvature(ArrayList<GeoHelper.Pt> csv) {
        int csvLength = csv.size();
        //List<Double> getCurvature = CsvUtil.countCurvature(afterCSV);      //得到该数据集中每个点对应的曲率
        List<double[]> getTranslationVector = CsvUtil.countNormK(csv);  //得到矢量点的集合
        List<double[]> getSpeedVector = CsvUtil.countSpeedVector(csv);  //得到速度矢量的集合
        double[] z = CsvUtil.countVectorProduct(getSpeedVector,getTranslationVector);  // 得到z的值，可以判断它向外侧还是内侧
        for (double[] dd: getTranslationVector){
            Log.d(TAG, "fromCurvature: ------>"+dd[0]+" , "+dd[1]);
        }
        //z 用来 判断 曲率的圆心指向的方向 ， 如果和平移的方向相反， 那就取负数 ， 原始数据点加上矢量乘以车宽
        List<double[]> translationAfterData = new ArrayList<>(afterCSV.size()); // 放新的计算后的数据
        for (int i = 0;i<csvLength;i++){
            //向矢量所指向的方向平移一个车身的距离
            double[] l =new double[2];
            if (z[i]<0){
                l[0] = csv.get(i).x+getTranslationVector.get(i)[0]*CarWidth;
                l[1] = csv.get(i).y+getTranslationVector.get(i)[1]*CarWidth;
            }else{
                l[0] = csv.get(i).x-getTranslationVector.get(i)[0]*CarWidth;
                l[1] = csv.get(i).y-getTranslationVector.get(i)[1]*CarWidth;
            }
            translationAfterData.add(l);
        }
        return translationAfterData;
    }

    /**
     * 把list打印成string，方便放入textView打印出来
     * @param list
     * @return
     */
    private StringBuilder printToTV(List<double[]> list){
        StringBuilder sb = new StringBuilder();
        for (double[] dd:list){
            String string = String.valueOf(dd[0]).concat("," + dd[1]) + "\n";
            sb.append(string);
        }
        return sb;
    }

    /**
     * 传入text，以显示在 提示 中
     *
     * @param text
     */
    public void toast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //初始化控件以及设置文本框可复制粘贴
    private void initBind() {
        translationBtn = findViewById(R.id.btn_translation);
        getDataBtn = findViewById(R.id.btn_get_data);
        btn3 = findViewById(R.id.btn_pingyi_c);
        showData = findViewById(R.id.reducedData);
        showData.setMovementMethod(ScrollingMovementMethod.getInstance());
        getDataBtn.setOnClickListener(this);
        translationBtn.setOnClickListener(this);
        btn3.setOnClickListener(this);

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
    }
    //检查权限，如果没有则跳转到权限申请页面
    private void checkPermission(){
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
}