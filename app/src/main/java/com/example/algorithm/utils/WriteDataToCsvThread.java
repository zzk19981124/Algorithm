package com.example.algorithm.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ak_
 * @desc 创建csv文件
 * @date 2021/7/26
 */
public class WriteDataToCsvThread extends Thread{
    short[] data;
    String fileName;
    String folder;
    StringBuilder sb;

    public WriteDataToCsvThread(short[] data, String fileName, String folder) {
        this.data = data;
        this.fileName = fileName;
        this.folder = folder;
    }

    /**
     * 创建目录
     */
    private void createFolder(){
        File fileDir = new File(folder); //通过将给定路径名字符串转换成抽象路径名来创建一个新 File 实例。
        boolean hasDir = fileDir.exists();
        if (!hasDir){
            fileDir.mkdirs(); //这里创建目录
        }
    }

    @Override
    public void run() {
        super.run();
        createFolder();
        File eFile = new File(folder+File.separator+fileName);
        if (!eFile.exists()){
            try {
                boolean newFire = eFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream os = new FileOutputStream(eFile,true);
            sb = new StringBuilder();
            for (int i =0;i<data.length;i++){
                sb.append(data[i]).append(",");
            }
            sb.append("\n");
            os.write(sb.toString().getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
