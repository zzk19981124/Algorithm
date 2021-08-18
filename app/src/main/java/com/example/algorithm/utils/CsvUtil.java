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
    private static Context context;
    private static GeoHelper.Pt pt;
    private static GeoHelper geoHelper = new GeoHelper();

    public CsvUtil(Activity activity) {
        context = activity.getApplicationContext();
    }

    public CsvUtil() {
    }

    /**
     * 处理手机文件夹中的csv文件
     *
     * @param csvPath
     * @return
     */
    public static ArrayList<GeoHelper.Pt> myself_csv(String csvPath) {

        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        String[] getstr;
        String[] getstr2;
        ArrayList<String[]> lists = new ArrayList<String[]>();
        try {
            //照着这个修改读取文件 https://blog.csdn.net/weixin_42119866/article/details/117488597?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-3.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-3.control
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    getstr = StringUtils.split(str, ",");
                    getstr2 = java.util.Arrays.copyOf(getstr, 2);
                    lists.add(getstr2);
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
            for (int ii = 0; ii < get_x_y.size(); ii++) {
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x, get_x_y.get(ii).y, 0);
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
     * 不带高度属性的
     *
     * @param csvPath
     * @return
     */
    public ArrayList<GeoHelper.Pt> fetch_csv(String csvPath) {
        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        String[] getstr;
        String[] getstr2;
        ArrayList<String[]> lists = new ArrayList<String[]>();
        try {
            is = new InputStreamReader(context.getAssets().open(csvPath));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens()) {
                    String str = st.nextToken();
                    getstr = StringUtils.split(str, ",");
                    getstr2 = java.util.Arrays.copyOf(getstr, 2);
                    lists.add(getstr2);
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
            for (int ii = 0; ii < get_x_y.size(); ii++) {
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x, get_x_y.get(ii).y, 0);
                //pt = geoHelper.Enu_FromWGS84(get_x_y.get(ii).x,get_x_y.get(ii).y,6371393);
                result.add(pt);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取带高度属性列的csv文件,转换局部坐标系
     *
     * @param csvPath
     * @return
     */
    public static ArrayList<GeoHelper.Pt> fetch_csv2(String csvPath) {
        InputStreamReader is;
        ArrayList<GeoHelper.Pt> get_x_y = new ArrayList<>();
        ArrayList<GeoHelper.Pt> result = new ArrayList<>();
        String[] getstr;
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
                    getstr = StringUtils.split(str, ",");
                    getstr2 = java.util.Arrays.copyOf(getstr, 3);
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
            jw = get_x_y;
            for (int ii = 0; ii < get_x_y.size(); ii++) {
                pt = new GeoHelper.Pt();
                pt = geoHelper.WGS84ToENU(get_x_y.get(ii).x, get_x_y.get(ii).y, get_x_y.get(ii).z);
                //pt = geoHelper.Enu_FromWGS84(get_x_y.get(ii).x,get_x_y.get(ii).y,6371393);
                result.add(pt);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static ArrayList<GeoHelper.Pt> jw = new ArrayList<>();//原始的经纬度坐标

    public static ArrayList<GeoHelper.Pt> getJw() {
        return jw;
    }

    public void setJw(ArrayList<GeoHelper.Pt> jw) {
        this.jw = jw;
    }
    /**
     * 计算曲率
     *
     * function [kappa,norm_k] = PJcurvature(x,y)
     *     x = reshape(x,3,1);
     *     y = reshape(y,3,1);
     *     t_a = norm([x(2)-x(1),y(2)-y(1)]);
     *     t_b = norm([x(3)-x(2),y(3)-y(2)]);
     *
     *     M =[[1, -t_a, t_a^2];
     *         [1, 0,    0    ];
     *         [1,  t_b, t_b^2]];
     *
     *     a = M\x
     *     b = M\y
     *
     *     kappa  = 2.*(a(3)*b(2)-b(3)*a(2)) / (a(2)^2.+b(2)^2.)^(1.5);
     *     norm_k =  [b(2),-a(2)]/sqrt(a(2)^2.+b(2)^2.);
     * end
     *
     * */
    private void countqulv(double x,double y){
        //x =fffffffffffffffffffffffffffffffffffffffff
        int xx = 1;
    }
}
