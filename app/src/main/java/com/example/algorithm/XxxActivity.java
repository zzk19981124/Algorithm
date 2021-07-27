package com.example.algorithm;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/27
 */
public class XxxActivity extends AppCompatActivity {
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
