package com.example.algorithm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algorithm.Helper.GeoHelper;
import com.example.algorithm.R;
import com.example.algorithm.utils.CsvUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FirstActivity-------->";
    private TextView tvSelectFile; //打开系统的文件目录，选中文件
    private Button btnUseLTTB;   // 根据tv中选中的文件放入函数中进行解析
    private String path; //2021-8-6 现在只能打开默认路径，打不开内部存储路径
    private List<String> mPermissionList = new ArrayList<>();
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_FORMAT_FILESYSTEMS};
    private static final int PERMISSION_REQUEST = 1;
    private List<GeoHelper.Pt> data = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String text = String.valueOf(msg.obj);
            if (text != null || text.equals("")) {
                btnUseLTTB.setText(msg.what + "");
            } else
                btnUseLTTB.setText(text);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermission();//检查文件读写权限
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
    }

    private void initView() {
        //浏览文件
        tvSelectFile = findViewById(R.id.tv_choose_file);
        btnUseLTTB = findViewById(R.id.btn_LTTB);
        tvSelectFile.setOnClickListener(this);
        btnUseLTTB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_file:
                //基本思路，先通过Android API调用系统自带的文件浏览器选取文件获得URI，然后将URI转换成file，从而得到file。
                Log.d(TAG, "onClick: ------choose tv to select file.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //只有设置了这个，返回的uri才能使用 getContentResolver().openInputStream(uri) 打开。
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_LTTB:
                if (path == null) {
                    Toast.makeText(this, "请先选择数据文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onClick: -------begin LTTB reduced data numbers.");
                delayThread();//延迟更新采集结果
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);    //经debug，在这获得了path
            } else {//4.4以下系统调用该方法
                path = getRealPathFromURI(uri);
            }
            Log.d(TAG, "绝对路径： " + path);
            tvSelectFile.setText(getFileName(path));//获取文件名
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * android 4.4 之后的从uri获取文件绝对路径的方法
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        if (context == null || uri == null) return null;
        final boolean isKitKat = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            //ExternalStorageProvider  外部存储提供者
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadProvider   下载提供者
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The uri to check
     * @return the uri 权限是不是
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 检查是否有这个权限
     *
     * @param uri
     * @return
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void checkPermission() {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /*
         * 判断是否为空
         * */
        if (mPermissionList.isEmpty()) {

        } else {
            //请求权限的方法
            String[] permissionss = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this, permissionss, PERMISSION_REQUEST);
        }
    }

    /**
     * 切割字符串，获取到最后的文件名
     */
    private String getFileName(String path) {
        String result = "";
        if (path != null) {
            String[] split = path.split("/");
            if (split != null && split.length != 0) {
                for (String string : split) {
                    result = string;
                }
            }
            return result;
        }
        return "未获取到path";
    }

    /**
     * 延迟更新，做采集数据点的工作。
     */
    private void delayThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i > -1; i--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(i);
                }
                //采集操作尝试
                data = CsvUtil.fetch_csv2(path);
                Log.d(TAG, "---------------> 数据集的长度：" + data.size());
            }
        }).start();
    }

    /**
     * 读取csv文件
     */
    private ArrayList readCsv() {
        ArrayList readerArr = new ArrayList<>();
        if (path == null || path.equals("")) return null;
        File file = new File(path);
        FileInputStream fileInputStream;
        Scanner in;
        try {
            fileInputStream = new FileInputStream(file);
            in = new Scanner(fileInputStream, "UTF-8");
            in.nextLine();
            while (in.hasNextLine()) {
                String[] lines = in.nextLine().split(",");
                readerArr.add(lines);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readerArr;
    }
}