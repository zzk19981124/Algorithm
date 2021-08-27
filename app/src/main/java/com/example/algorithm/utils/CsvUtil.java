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
     * 当csv文件中的数据  已经是局部坐标系时使用
     * 读取csv，放入集合中
     *
     * @param csvPath
     * @return
     */
    public static ArrayList<GeoHelper.Pt> cutDataNoToENU(String csvPath,Context context) {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return get_x_y;
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
     * <p>
     * function [kappa,norm_k] = PJcurvature(x,y)
     * x = reshape(x,3,1);
     * y = reshape(y,3,1);
     * t_a = norm([x(2)-x(1),y(2)-y(1)]);
     * t_b = norm([x(3)-x(2),y(3)-y(2)]);
     * <p>
     * M =[[1, -t_a, t_a^2];
     * [1, 0,    0    ];
     * [1,  t_b, t_b^2]];
     * <p>
     * a = M\x
     * b = M\y
     * <p>
     * kappa  = 2.*(a(3)*b(2)-b(3)*a(2)) / (a(2)^2.+b(2)^2.)^(1.5);
     * norm_k =  [b(2),-a(2)]/sqrt(a(2)^2.+b(2)^2.);
     * end
     */
    private void countqulv(double x, double y) {
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
        int dataLength = data.size();
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
            double t_a = norm(x1, x2, y1, y2);
            //第二个点和第三个点之间的距离
            double t_b = norm(x2, x3, y2, y3);
            // dense  是 M
            Matrix dense = DenseMatrix.Factory.zeros(3, 3);

            for (int j = 0; j < 3; j++) {
                dense.setAsDouble(1, i, 0);
            }
            for (int k = 1; k < 3; k++) {
                dense.setAsDouble(0, 1, k);
            }
            dense.setAsDouble(-t_a, 0, 1);
            dense.setAsDouble(t_a * t_a, 0, 2);
            dense.setAsDouble(t_b, 2, 1);
            dense.setAsDouble(t_b * t_b, 2, 2);

            Matrix x = DenseMatrix.Factory.zeros(3, 1);
            x.setAsDouble(x1, 0, 0);
            x.setAsDouble(x2, 1, 0);
            x.setAsDouble(x3, 2, 0);

            Matrix y = DenseMatrix.Factory.zeros(3, 1);
            y.setAsDouble(y1, 0, 0);
            y.setAsDouble(y2, 1, 0);
            y.setAsDouble(y3, 2, 0);

            Matrix a = (dense.inv()).mtimes(x);
            Matrix b = (dense.inv()).mtimes(y);

            double a1 = a.getAsDouble(0, 0);
            double a2 = a.getAsDouble(1, 0);
            double a3 = a.getAsDouble(2, 0);

            double b1 = b.getAsDouble(0, 0);
            double b2 = b.getAsDouble(1, 0);
            double b3 = b.getAsDouble(2, 0);
            //最终曲率
            double curvature = (2 * (a3 * b2 - a2 * b3)) / ((Math.pow(a2, 2) + Math.pow(b2, 2)) * Math.sqrt((Math.pow(a2, 2) + Math.pow(b2, 2))));
            allCurvature.set(i, curvature);

            //将首尾赋值他们相邻的数
            if (i == 1) {
                allCurvature.set(0, curvature);
            } else if (i == dataLength - 2) {
                //allCurvature.set(dataLength - 1,curvature);
                allCurvature.add(curvature);
            }
            List<Double[]> list = new ArrayList<>();
            Double[] l = new Double[2];
            l[0] = b2 / (Math.sqrt(Math.pow(a2, 2) + Math.pow(b2, 2)));
            l[1] = a2 / (Math.sqrt(Math.pow(a2, 2) + Math.pow(b2, 2)));
            list.add(l);


        }

        return allCurvature;
    }

    /**
     * 返回曲线的每个点的矢量
     */
    public static List<double[]> countNormK(ArrayList<GeoHelper.Pt> data) {
        int dataLength = data.size();
        List<double[]> list = new ArrayList<>();
        double dd[] = {0.0, 0.0};
        for (int i = 0; i < dataLength; i++) {
            //list.set(i,dd);
            //list.get(i) = dd;
            list.add(dd);
        }

        //ArrayList<GeoHelper.Pt> curvedData = new ArrayList<>(dataLength);
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
            double t_a = norm(x1, x2, y1, y2);
            //第二个点和第三个点之间的距离
            double t_b = norm(x2, x3, y2, y3);
            // dense  是 M
            Matrix dense = DenseMatrix.Factory.zeros(3, 3);

            for (int j = 0; j < 3; j++) {
                dense.setAsDouble(1, j, 0);
            }
            for (int k = 1; k < 3; k++) {
                dense.setAsDouble(0, 1, k);
            }
            dense.setAsDouble(-t_a, 0, 1);
            dense.setAsDouble(t_a * t_a, 0, 2);
            dense.setAsDouble(t_b, 2, 1);
            dense.setAsDouble(t_b * t_b, 2, 2);

            //System.out.println(dense);

            Matrix x = DenseMatrix.Factory.zeros(3, 1);
            x.setAsDouble(x1, 0, 0);
            x.setAsDouble(x2, 1, 0);
            x.setAsDouble(x3, 2, 0);

            Matrix y = DenseMatrix.Factory.zeros(3, 1);
            y.setAsDouble(y1, 0, 0);
            y.setAsDouble(y2, 1, 0);
            y.setAsDouble(y3, 2, 0);

            Matrix a = (dense.inv()).mtimes(x);
            Matrix b = (dense.inv()).mtimes(y);

            double a1 = a.getAsDouble(0, 0);
            double a2 = a.getAsDouble(1, 0);
            double a3 = a.getAsDouble(2, 0);

            double b1 = b.getAsDouble(0, 0);
            double b2 = b.getAsDouble(1, 0);
            double b3 = b.getAsDouble(2, 0);

            double[] l = new double[2];
            l[0] = b2 / (Math.sqrt(Math.pow(a2, 2) + Math.pow(b2, 2)));
            l[1] = a2 / (Math.sqrt(Math.pow(a2, 2) + Math.pow(b2, 2)));

            double setToNorm = norm(l[0],l[1]);

            l[0] = l[0]/setToNorm;
            l[1] = l[1]/setToNorm;

            list.set(i, l);

            if (i == 1) {
                list.set(0, l);
            } else if (i == dataLength - 2) {
                list.set(dataLength - 1, l);
                //list.add(l);
            }
        }
        return list;
    }

    /**
     * 计算速度矢量，i+1的点 - i的点
     *
     * @param csvData
     */
    public static List<double[]> countSpeedVector(ArrayList<GeoHelper.Pt> csvData) {
        int dataLength = csvData.size();
        List<double[]> cutData = threeToTwo(csvData);



        for (int i = 0; i < dataLength - 1; i++) {
            double[] thisPoint = cutData.get(i);
            double[] nextPoint = cutData.get(i + 1);

            double[] vector = new double[2];
            vector[0] = nextPoint[0] - thisPoint[0];
            vector[1] = nextPoint[1] - thisPoint[1];

            //向量归一化
            double setToNorm = norm(vector[0],vector[1]);
            vector[0] = vector[0]/setToNorm;

            vector[1] = vector[1]/setToNorm;

            cutData.set(i, vector);
            if (i == dataLength - 2) {
                cutData.set(dataLength - 1, vector);
            }
        }



        return cutData;
    }

    /**
     * 向量归一化
     */
    private static double norm(double x, double y){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    /**
     * 计算向量积，也就是两个矢量的差乘
     * 注意传进来的是两列，在最后面要再加一列0
     * a = [a1,a2,a3]
     * b = [b1,b2,b3]
     * a^b = [a2b3 - a3b2 , a3b1 - a1b3 , a1b2 - a2b1]
     * a, 是速度矢量
     * b, 是曲率矢量
     *
     * @param a
     * @param b
     */
    public static double[] countVectorProduct(List<double[]> a, List<double[]> b) {
        double[] result = new double[a.size()];
        for (int i = 0; i < a.size(); i++) {
            double a1 = a.get(i)[0];
            double a2 = a.get(i)[1];
            double b1 = b.get(i)[0];
            double b2 = b.get(i)[1];
            result[i] = a1 * b2 - a2 * b1;
        }
        return result;
    }


    /**
     * 如果数据是三列，取前两列，放入List<double[]> 中
     *
     * @param data
     */
    public static List<double[]> threeToTwo(ArrayList<GeoHelper.Pt> data) {
        int dataLength = data.size();
        List<double[]> list = new ArrayList<>(dataLength);
        for (int i = 0; i < dataLength; i++) {
            double[] l = new double[2];
            l[0] = data.get(i).x;
            l[1] = data.get(i).y;
            list.add(l);
        }
        return list;
    }

    /**
     * 求范数
     */
    private static double norm(double x1, double x2, double y1, double y2) {
        //x2-x1,y2-y1
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    //平移曲线，使用局部坐标系,enu东北天
    public static ArrayList<GeoHelper.Pt> translationNEU(ArrayList<GeoHelper.Pt> lineCSV,
                                                   int direction, double distance) {
        ArrayList<GeoHelper.Pt> originalData = new ArrayList<>(lineCSV.size());
        //originalData = lineCSV;
        switch (direction) {
            case 1://北
                for (GeoHelper.Pt data : originalData) {
                    data.y += distance;
                }
                break;
            case 2://南
                for (GeoHelper.Pt data : originalData) {
                    data.y -= distance;
                }
                break;
            case 3://西
                for (GeoHelper.Pt data : originalData) {
                    data.x -= distance;
                }
                break;
            case 4://东
                for (int i =0;i<lineCSV.size();i++) {

                    GeoHelper.Pt p = new GeoHelper.Pt();
                    p.x = lineCSV.get(i).x +distance;
                    p.y = lineCSV.get(i).y+ distance;
                    originalData.add(p);
                }
                break;
            case 5://东北
                for (GeoHelper.Pt data : originalData) {
                    data.x += distance/Math.sqrt(2);
                    data.y+=distance/Math.sqrt(2);
                }
            default:
                break;
        }
        return originalData;
    }
}
