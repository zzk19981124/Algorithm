package com.example.algorithm;

import com.example.algorithm.Helper.GeoHelper;
import org.junit.Test;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final double R = 1.5; //车身的一半

    //private static final double
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
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
    @Test
    public List<Double> countTest(ArrayList<GeoHelper.Pt> data) {
        List<Double> allCurvature = new ArrayList<>();
        if (data.size() == 0) return allCurvature;
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
            double t_a = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
            //第二个点和第三个点之间的距离
            double t_b = Math.sqrt(Math.pow((x3-x2),2)+Math.pow((y3-y2),2));
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
            allCurvature.add(curvature);
        }

        return allCurvature;
    }
    @Test
    public void getTest() {
        Matrix dense = DenseMatrix.Factory.zeros(3,3);
        System.out.println(dense);
    }
}