package com.example.algorithm.utils;

import android.app.Activity;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author hello word
 * @desc 处理csv文件
 * @date 2021/7/21
 */
public class CsvUtil {
    private Activity activity;
    private Context context;
    private ArrayList<String[]> lists = new ArrayList<String[]>();
    private String getStr[] = new String[2];

    public CsvUtil(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
    }

    /**
     * 分割csv文件
     *
     * @param csvPath
     * @return
     */
    public ArrayList<String[]> fetch_csv(String csvPath) {
        InputStreamReader is;
        ArrayList<long[]> get_x_y = new ArrayList<>();
        try {
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    //getStr = str.split(",");
                    getStr = StringUtils.split(str,",");
                    lists.add(getStr);
                }
            }
            if (lists != null) {
                for (int i = 0; i < lists.size(); i++) {
                    for (int j = 0; j < 2; j++) {
                        //get_x_y.get(i)[j] = Long.parseLong(lists.get(i)[j]);
                        //System.out.println(get_x_y.get(i)[j]);
                    }
                }
            }
            //get_x_y.get(0)[0] = 0;
            //get_x_y.get(0)[1] = 0;
            /*if (get_x_y!=null){
                for (int i =0;i<get_x_y.size();i++){
                    for (int j = 0;j<2;j++){

                    }
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }

    /**
     * 将经纬度坐标转换成平面直角坐标系
     * 经度是x，纬度是y
     */
    public ArrayList<long[]> Convert_coordinates(ArrayList<String[]> lists) {
        ArrayList<long[]> get_x_y = new ArrayList<>();
        if (lists != null) {
            for (int i = 0; i < lists.size(); i++) {
                for (int j = 0; j < 2; j++) {
                    get_x_y.get(i)[j] = Long.parseLong(lists.get(i)[j]);
                }
            }
        }
        get_x_y.get(0)[0] = 0;
        get_x_y.get(0)[1] = 0;
        if (get_x_y != null) {
            for (int i = 0; i < get_x_y.size(); i++) {
                for (int j = 0; j < 2; j++) {

                }
            }
        }

        return get_x_y;
    }
}
