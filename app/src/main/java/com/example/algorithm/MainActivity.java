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
    private Button kBtnData,createDbBtn;
    private ArrayList<GeoHelper.Pt> csvLists = new ArrayList<>();
    private CsvUtil csvUtil;
    //private String filename = "reducedData.txt";
    private TextView showData;
    //private String filepath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Test"  + File.separator;
    private List<String> mPermissionList = new ArrayList<>();
    ArrayList<GeoHelper.Pt> get = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> csvList1 = new ArrayList<>();
    private ArrayList<GeoHelper.Pt> csvList2 = new ArrayList<>();
    private String[] permissions={Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        //android11 新特性，需要强制启用
        //XXPermissions.setScopedStorage(true);
        setContentView(R.layout.activity_main);
        //checkPermissionAndroid11();
        //initPermission();
        initBind();
        //mDB = new MyDBOpenHelper(this,"reducedData.db",null,1);

       // csvLists = csvUtil.fetch_csv("convert.csv");
        System.out.println("------------csv有"+csvLists.size()+"组数据----------------");

        //get = LTTB.getLTTB(csvLists,500);
        System.out.println("------------csv有"+get.size()+"组数据----------------");
    }

    private void initBind() {
        kBtnData = findViewById(R.id.btn_create_db);
        //getPermissionBtn.setSingleLine(false);
        createDbBtn = findViewById(R.id.btn_getPermission);
        //createDbBtn.setVisibility(View.GONE);
        showData = findViewById(R.id.reducedData);
        showData.setMovementMethod(ScrollingMovementMethod.getInstance());
        int _sdkLevel = Build.VERSION.SDK_INT;
        if (_sdkLevel>=11){
            showData.setTextIsSelectable(true);
        }else{
            showData.setFocusableInTouchMode(true);
            showData.setFocusable(true);
            showData.setClickable(true);
            showData.setLongClickable(true);
            showData.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            showData.setText(showData.getText(), TextView.BufferType.SPANNABLE);
        }
        //检查动态权限
        //mPermissionHelper = new PermissionHelper(this,MY_PERMISSION_REQUEST_CODE);
        csvUtil = new CsvUtil(this);
        kBtnData.setOnClickListener(this);
        createDbBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_db:
                StringBuilder sb = new StringBuilder();
                for (int i = 0;i<get.size();i++){
                    String s1 = String.valueOf(get.get(i).x);
                    String s2 = String.valueOf(get.get(i).y);
                    String s3 = String.valueOf(get.get(i).z);
                    String ss  = s1+","+s2+","+s3+"\n";
                    sb.append(ss);
                }
                showData.setText(sb);
                break;
            case R.id.btn_getPermission:
                /*Message msg1 = new Message();
                msg1.what = 1;*/
                csvList1 = csvUtil.fetch_csv2("1.csv");
                System.out.println("1.csv---------->"+csvList1.size());
                csvList2 = LTTB.getLTTB(csvList1,150);
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0;i<csvList2.size();i++){
                    String s1 = String.valueOf(csvList2.get(i).x);
                    String s2 = String.valueOf(csvList2.get(i).y);
                    String s3 = String.valueOf(csvList2.get(i).z);
                    String ss  = s1+","+s2+","+s3+"\n";
                    sb1.append(ss);
                }
                showData.setText(sb1);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST:
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
        }
    }
    private void checkPermission(){
        mPermissionList.clear();
        for (int i =0;i<permissions.length;i++){
            if (ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permissions[i]);
            }
        }
        /*
        * 判断是否为空
        * */
        if (mPermissionList.isEmpty()){

        }else{
            //请求权限的方法
            String[] permissionss = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissionss,PERMISSION_REQUEST);
        }
    }
    /*
    * android11 存储权限
    * */
    private void checkPermissionAndroid11(){
        XXPermissions.with(this)
                //.permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            toast("获取存储权限成功~");
                        }else{
                            toast("获取部分权限成功，但部分权限未正常授予。");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never){
                            toast("被永久拒绝授权，请手动授予读写权限");
                            //跳转到应用权限系统页面
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        XXPermissions.startPermissionActivity(MainActivity.this,permissions);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }else{
                            toast("获取读写权限失败！");
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE){
            if (XXPermissions.isGranted(this, Permission.MANAGE_EXTERNAL_STORAGE)&&
                    XXPermissions.isGranted(this,Permission.Group.STORAGE)){
                toast("用户已经在权限设置页授予了读写权限");
            }else{
                toast("用户没有在权限设置页授予读写权限");
            }
        }
    }

    public void toast(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}