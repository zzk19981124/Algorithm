package com.example.algorithm.utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.algorithm.Helper.GeoHelper;

import org.apache.commons.lang3.StringUtils;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

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
    public ArrayList<GeoHelper.Pt>  fetch_csv(String csvPath) {
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

    /**
     * (x-x0)^2 - (y-y0)^2 = r^2
     * 利用离散曲率计算得出的曲线和直接平移的曲线做对比  ：https://zhuanlan.zhihu.com/p/72083902      https://zhuanlan.zhihu.com/p/138618177
     * 参数中放入数据集合
     * 三个点计算中间点的曲率及圆心
     * 返回值是加上指向圆心的矢量值后的点集
     * 去掉首尾两个点计算
     * 矩阵运算 https://blog.csdn.net/lionel_fengj/article/details/53400715
     */

    public static List<Double> countCurvature(ArrayList<GeoHelper.Pt> data) {
        List<Double> allCurvature = new ArrayList<>(data.size());
        //allCurvature.add(0.0);
       // allCurvature.set(data.size()-1,0.0);
        //if (data.size() == 0) return allCurvature;
        int dataLength = data.size();
        /*for (Double d: allCurvature){

        }*/
        ArrayList<GeoHelper.Pt> curvedData = new ArrayList<>(dataLength);
        //计算第二个点~倒数第二个点 之间的数据
        for (int i = 1; i < dataLength - 1; i++) {
            /*
             * 使用公式  A = M^-1*X
             *          B = M^-1*Y
             * */

            GeoHelper.Pt beforePoint = data.get(i - 1);
            GeoHelper.Pt thisPoint = data.get(i);
            GeoHelper.Pt nextPoint = data.get(i + 1);

            double x1 = beforePoint.x;
            double y1 = beforePoint.y;
            double x2 = thisPoint.x;
            double y2 = thisPoint.y;
            double x3 = nextPoint.x;
            double y3 = nextPoint.y;
            //第一个点和第二个点之间的距离
            double t_a = norm(x1,x2,y1,y2);
            //第二个点和第三个点之间的距离
            double t_b = norm(x2,x3,y2,y3);
            // dense  是 M
            Matrix dense = DenseMatrix.Factory.zeros(3,3);

            for (int j =0;j<3;j++){
                dense.setAsDouble(1,i,0);
            }
            for (int k = 1;k<3;k++){
                dense.setAsDouble(0,1,k);
            }
            dense.setAsDouble(-t_a,0,1);
            dense.setAsDouble(t_a*t_a,0,2);
            dense.setAsDouble(t_b,2,1);
            dense.setAsDouble(t_b*t_b,2,2);

            Matrix x = DenseMatrix.Factory.zeros(3,1);
            x.setAsDouble(x1,0,0);
            x.setAsDouble(x2,1,0);
            x.setAsDouble(x3,2,0);

            Matrix y = DenseMatrix.Factory.zeros(3,1);
            y.setAsDouble(y1,0,0);
            y.setAsDouble(y2,1,0);
            y.setAsDouble(y3,2,0);

            Matrix a = (dense.inv()).mtimes(x);
            Matrix b = (dense.inv()).mtimes(y);

            double a1 = a.getAsDouble(0,0);
            double a2 = a.getAsDouble(1,0);
            double a3 = a.getAsDouble(2,0);

            double b1 = b.getAsDouble(0,0);
            double b2 = b.getAsDouble(1,0);
            double b3 = b.getAsDouble(2,0);
            //最终曲率
            double curvature = (2*(a3*b2-a2*b3))/((Math.pow(a2,2)+Math.pow(b2,2))*Math.sqrt((Math.pow(a2,2)+Math.pow(b2,2))));
            allCurvature.set(i,curvature);

            //将首尾赋值他们相邻的数
            if (i==1){
                allCurvature.set(0,curvature);
            }else if (i==dataLength - 2){
                //allCurvature.set(dataLength - 1,curvature);
                allCurvature.add(curvature);
            }
           List<Double[]> list = new ArrayList<>();
            Double[] l = new Double[2];
            l[0] = b2/(Math.sqrt(Math.pow(a2,2)+Math.pow(b2,2)));
            l[1] = a2/(Math.sqrt(Math.pow(a2,2)+Math.pow(b2,2)));
            list.add(l);


        }

        return allCurvature;
    }
    /**
     *
     *返回曲线的每个点的矢量
     */
    public static List<double[]> countNormK (ArrayList<GeoHelper.Pt> data) {
        int dataLength = data.size();
        List<double[]> list = new ArrayList<>(dataLength);
        double dd[] = {0.0,0.0};
        for (int i =0;i<dataLength;i++){
            list.add(dd);
        }

        ArrayList<GeoHelper.Pt> curvedData = new ArrayList<>(dataLength);
        //计算第二个点~倒数第二个点 之间的数据
        for (int i = 1; i < dataLength - 1; i++) {

            /*
             * 使用公式  A = M^-1*X
             *          B = M^-1*Y
             * */

            GeoHelper.Pt beforePoint = data.get(i - 1);
            GeoHelper.Pt thisPoint = data.get(i);
            GeoHelper.Pt nextPoint = data.get(i + 1);

            double x1 = beforePoint.x;
            double y1 = beforePoint.y;
            double x2 = thisPoint.x;
            double y2 = thisPoint.y;
            double x3 = nextPoint.x;
            double y3 = nextPoint.y;
            //第一个点和第二个点之间的距离
            double t_a = norm(x1,x2,y1,y2);
            //第二个点和第三个点之间的距离
            double t_b = norm(x2,x3,y2,y3);
            // dense  是 M
            Matrix dense = DenseMatrix.Factory.zeros(3,3);

            for (int j =0;j<3;j++){
                dense.setAsDouble(1,j,0);
            }
            for (int k = 1;k<3;k++){
                dense.setAsDouble(0,1,k);
            }
            dense.setAsDouble(-t_a,0,1);
            dense.setAsDouble(t_a*t_a,0,2);
            dense.setAsDouble(t_b,2,1);
            dense.setAsDouble(t_b*t_b,2,2);

            //System.out.println(dense);

            Matrix x = DenseMatrix.Factory.zeros(3,1);
            x.setAsDouble(x1,0,0);
            x.setAsDouble(x2,1,0);
            x.setAsDouble(x3,2,0);

            Matrix y = DenseMatrix.Factory.zeros(3,1);
            y.setAsDouble(y1,0,0);
            y.setAsDouble(y2,1,0);
            y.setAsDouble(y3,2,0);

            Matrix a = (dense.inv()).mtimes(x);
            Matrix b = (dense.inv()).mtimes(y);

            double a1 = a.getAsDouble(0,0);
            double a2 = a.getAsDouble(1,0);
            double a3 = a.getAsDouble(2,0);

            double b1 = b.getAsDouble(0,0);
            double b2 = b.getAsDouble(1,0);
            double b3 = b.getAsDouble(2,0);

            double[] l = new double[2];
            l[0] = b2/(Math.sqrt(Math.pow(a2,2)+Math.pow(b2,2)));
            l[1] = a2/(Math.sqrt(Math.pow(a2,2)+Math.pow(b2,2)));
            list.set(i,l);

            if (i==1){
                list.set(0,l);
            }else if (i==dataLength-2){
                //list.set(dataLength-1,l);
                list.add(l);
            }
        }
        return list;
    }

    /**
     * 求范数
     * */
    private static double norm(double x1,double x2,double y1,double y2){
        //x2-x1,y2-y1
        return Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
    }
}
