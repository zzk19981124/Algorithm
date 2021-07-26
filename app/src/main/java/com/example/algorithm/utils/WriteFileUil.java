package com.example.algorithm.utils;

import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author ak_
 * @desc 写出为txt文件
 * @date 2021/7/26
 */
public class WriteFileUil {
    //将字符串写入到文本文件中
    public void writeTxtToFile(String strContent,String filepath,String filename){
        //生成文件夹之后，再生成文件，防止出错
        makeFilePath(filepath,filename);

        String strFilePath = filepath+filename;
        //每次写入时都换行
        String strcontent = strContent+"\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()){
                Log.d("TestFile", "Create the file: "+ strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file,"rwd");
            raf.seek(file.length());
            raf.write(strcontent.getBytes());
            raf.close();
        } catch (Exception exception) {
            Log.e("testFile", "error on write File: "+exception);
        }
    }
    //生成文件
    public File makeFilePath(String filepath,String filename){
        File file = null;
        makeRootDirectory(filepath);
        try {
            file = new File(filepath+filename);
            if (!file.exists()){
                //这儿报错
                file.createNewFile();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }
    //生成文件夹
    public static void makeRootDirectory(String filepath){
        File file = null;
        try {
            file = new File(filepath);
            if (!file.exists()){
                file.mkdirs();
            }
        } catch (Exception exception) {
            Log.i("error", exception+"");
        }
    }
}
// 11111111111111
/*
* 1
* 2
* 2
* 3
* 4
* */
