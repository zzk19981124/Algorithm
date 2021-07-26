package com.example.algorithm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.algorithm.Helper.GeoHelper;
import com.example.algorithm.Helper.MyDBOpenHelper;
import com.example.algorithm.Helper.PermissionHelper;
import com.example.algorithm.lttb.LTTB;
import com.example.algorithm.utils.CsvUtil;
import com.example.algorithm.utils.WriteDataToCsvThread;
import com.example.algorithm.utils.WriteFileUil;
import com.zyq.easypermission.EasyPermission;
import com.zyq.easypermission.EasyPermissionHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.SNIHostName;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST = 1;
    private static final String TAG = "MainActivity";
    private static final String FILE_CSV = "reduced_data";
    private static final String FILE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "AboveView" + File.separator + "data";
    private Button getPermissionBtn,createDbBtn;
    private ArrayList<GeoHelper.Pt> csvLists = new ArrayList<>();
    private CsvUtil csvUtil;
    private GeoHelper geoHelper = new GeoHelper();
    private MyDBOpenHelper mDB;
   // private PermissionHelper mPermissionHelper;
    private final int MY_PERMISSION_REQUEST_CODE = 404;
    private String filename = "log.txt";
    private String filepath = "/sdcard/Test/";
    private WriteFileUil writeTxt;
    private List<String> mPermissionList = new ArrayList<>();
    private String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_FORMAT_FILESYSTEMS};
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    for (int i = 0;i<csvLists.size();i++){
                        String s = csvLists.get(i).x+","+csvLists.get(i).y+","+csvLists.get(i).z;
                        writeTxt.writeTxtToFile(s,filepath,filename);
                    }//writeTxt.writeTxtToFile("s",filepath,filename);
                    Toast.makeText(MainActivity.this,"生成txt文件成功",Toast.LENGTH_SHORT).show();
                    break;
                case 1:

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initPermission();
        initBind();
        mDB = new MyDBOpenHelper(this,"reducedData.db",null,1);

        csvLists = csvUtil.fetch_csv("convert.csv");
        System.out.println("------------csv有"+csvLists.size()+"组数据----------------");

        ArrayList<GeoHelper.Pt> get = new ArrayList<>();
        get = LTTB.getLTTB(csvLists,5000);
        for (int i =0;i<10;i++){
            Log.d(TAG, String.valueOf(get.get(i)));
        }
    }

    private void initBind() {
        writeTxt = new WriteFileUil();
        getPermissionBtn = findViewById(R.id.btn_create_db);
        createDbBtn = findViewById(R.id.btn_getPermission);
        //检查动态权限
        //mPermissionHelper = new PermissionHelper(this,MY_PERMISSION_REQUEST_CODE);
        csvUtil = new CsvUtil(this);
        getPermissionBtn.setOnClickListener(this);
        createDbBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_db:
                //mDB.getWritableDatabase();
                Message msg0 = new Message();
                msg0.what = 0;
                mHandler.sendMessage(msg0);

                break;
            case R.id.btn_getPermission:
                Message msg1 = new Message();
                msg1.what = 1;
                mHandler.sendMessage(msg1);
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
    //222222
}