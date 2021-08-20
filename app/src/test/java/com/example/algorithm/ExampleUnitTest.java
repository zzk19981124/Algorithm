package com.example.algorithm;

import android.graphics.Point;

import com.example.algorithm.Helper.GeoHelper;

import org.junit.Test;
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

import java.util.ArrayList;

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
    public ArrayList<GeoHelper.Pt> countTest(ArrayList<GeoHelper.Pt> data) {
        if (data.size() == 0) return data;
        int dataLength = data.size();
        ArrayList<GeoHelper.Pt> curvedData = new ArrayList<>(dataLength);
        //计算第二个点~倒数第二个点 之间的数据
        for (int i = 1; i < dataLength - 1; i++) {
            GeoHelper.Pt beforePoint = data.get(i - 1);
            GeoHelper.Pt thisPoint = data.get(i);
            GeoHelper.Pt nextPoint = data.get(i + 1);
            //第一个点和第二个点之间的距离
            double t_a = Math.sqrt((thisPoint.x-beforePoint.x)*(thisPoint.x-beforePoint.x)+(thisPoint.y-beforePoint.y)*(thisPoint.y-beforePoint.y));
            //第二个点和第三个点之间的距离
            double t_b = Math.sqrt((nextPoint.x-thisPoint.x)*(nextPoint.x-thisPoint.x)+(nextPoint.y-thisPoint.y)*(nextPoint.y-thisPoint.y));

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


        }
        return curvedData;
    }
    @Test
    public void getTest() {
        Matrix dense = DenseMatrix.Factory.zeros(3,3);
        System.out.println(dense);
    }
}