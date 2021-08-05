package com.example.algorithm.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.algorithm.Helper.GeoHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private static   Context context;
    private static ArrayList<String[]> lists = new ArrayList<String[]>();
    private static String getStr[] = new String[2];
    private static GeoHelper.Pt pt;
    private static String[] getStr2;
    private static GeoHelper geoHelper = new GeoHelper();
    public CsvUtil(Activity activity) {
        context = activity.getApplicationContext();
    }
    public CsvUtil() { }

    /**
     * 处理手机文件夹中的csv文件
     * @param csvPath
     * @return
     */
    public  static ArrayList<GeoHelper.Pt> myself_csv(String csvPath) {
        //全部初始化
        for (int i =0;i<getStr.length;i++){
            getStr[i] = "";
        }
        for (int i =0;i<getStr2.length;i++){
            getStr2[i] = "";
        }
        for (int i =0;i<lists.size();i++){
            for (int j = 0;j<lists.get(i).length;j++){
                lists.get(i)[j] = "";
            }
        }
        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        try {
            //照着这个修改读取文件
            //https://blog.csdn.net/weixin_42119866/article/details/117488597?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-3.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-3.control
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    getStr = StringUtils.split(str,",");
                    getStr2 = java.util.Arrays.copyOf(getStr,2);
                    lists.add(getStr2);
                }
            }
            if (lists != null) {
                for (int i = 0; i < lists.size(); i++) {
                    double x = 0;
                    double y = 0;
                    pt = new GeoHelper.Pt();
                    for (int j = 0; j < 2; j++) {
                        x = Double.parseDouble(lists.get(i)[0]);
                        y = Double.parseDouble(lists.get(i)[1]);
                    }
                    pt.x = x;
                    pt.y = y;
                    get_x_y.add(pt);
                }
            }
            for (int ii =0;ii<get_x_y.size();ii++){
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x,get_x_y.get(ii).y,0);
                //pt = geoHelper.Enu_FromWGS84(get_x_y.get(ii).x,get_x_y.get(ii).y,6371393);
                result.add(pt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 分割csv文件
     *  不带高度属性的
     * @param csvPath
     * @return
     */
    public  ArrayList<GeoHelper.Pt> fetch_csv(String csvPath) {
        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        try {
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    getStr = StringUtils.split(str,",");
                    getStr2 = java.util.Arrays.copyOf(getStr,2);
                    lists.add(getStr2);
                }
            }
            if (lists != null) {
                for (int i = 0; i < lists.size(); i++) {
                    double x = 0;
                    double y = 0;
                    pt = new GeoHelper.Pt();
                    for (int j = 0; j < 2; j++) {
                        x = Double.parseDouble(lists.get(i)[0]);
                        y = Double.parseDouble(lists.get(i)[1]);
                    }
                    pt.x = x;
                    pt.y = y;
                    get_x_y.add(pt);
                }
            }
            for (int ii =0;ii<get_x_y.size();ii++){
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x,get_x_y.get(ii).y,0);
                //pt = geoHelper.Enu_FromWGS84(get_x_y.get(ii).x,get_x_y.get(ii).y,6371393);
                result.add(pt);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 带高度属性的
     * @param csvPath
     * @return
     */
    public static ArrayList<GeoHelper.Pt> fetch_csv2(String csvPath) {
        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        String[] getstr = new String[3];
        String[] getstr2;
        ArrayList<String[]> list = new ArrayList<String[]>();
        try {
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    getstr = StringUtils.split(str,",");
                    getstr2 = java.util.Arrays.copyOf(getstr,3);
                    list.add(getstr2);
                }
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    double x = 0;
                    double y = 0;
                    double z = 0;
                    pt = new GeoHelper.Pt();
                    for (int j = 0; j < 2; j++) {
                        x = Double.parseDouble(list.get(i)[0]);
                        y = Double.parseDouble(list.get(i)[1]);
                        z = Double.parseDouble(list.get(i)[2]);
                    }
                    pt.x = x;
                    pt.y = y;
                    pt.z = z;
                    get_x_y.add(pt);
                }
            }
            for (int ii =0;ii<get_x_y.size();ii++){
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x,get_x_y.get(ii).y,get_x_y.get(ii).z);
                //pt = geoHelper.Enu_FromWGS84(get_x_y.get(ii).x,get_x_y.get(ii).y,6371393);
                result.add(pt);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
