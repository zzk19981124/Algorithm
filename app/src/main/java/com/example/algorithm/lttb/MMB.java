package com.example.algorithm.lttb;

import com.example.algorithm.Helper.GeoHelper;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author hello word
 * @desc 第一种算法 Mode-Median-Bucket
 * @date 2021/7/21
 */
public class MMB {
    /**
     *  1.将数据分割为与阈值相同数量的桶
     *  2.对于每一个桶执行，（选择一个数据点用来代表桶）
     *  if（在这个桶中发现了整体中的最大值或最小值）{
     *      使用该数据点
     *  }
     * if（有一个最多次出现的y值——众数）{
     *    if（仅有一个对应点）{
     *        使用它
     *    }else{
     *        使用x值最低的那个点
     *    }
     * }else{
     *     if(桶里的数据点的量是奇数){
     *         使用中间对应的数据点
     *     }else{
     *         使用y值对偏左的那个数据点
     *     }
     * }
     *
     *最后确保原始数据的第一个数据点，也是下采样数据中的第一个和最后一个数据点
     *WGS-84——ENU  经纬高 转 东北天 ，东是x轴，北是y轴，天这里默认0
     */
    public ArrayList<GeoHelper.Pt> getSplit(ArrayList<GeoHelper.Pt> data, int threshold){
        //传进去的是x，y坐标
        ArrayList<GeoHelper.Pt> result = new ArrayList<>(threshold);
        int dataLength = data.size();
        if (threshold >= dataLength || threshold==0){
            return data;
        }
        //点集，去掉首位点和末位点
        double everyBucket = (dataLength-2)/(threshold-2);
        //默认添加首点
        int a = 0;
        result.add(data.get(a));
        //找出全局最大值和最小值
        double allY[] = new double[dataLength];
        for (int as = 0;as<dataLength;as++){
            allY[as] = data.get(as).y;
        }
        //获得最大值
        double theMax = getMax(allY);
        //获得最小值
        double theMin = getMin(allY);
        for (int i =0;i<threshold-2;i++){
            //在这里执行每个桶

            int bucketStart = (int) (i*everyBucket)+1;
            int bucketEnd = (int) ((i+1)*everyBucket)+1;
            bucketEnd = bucketEnd < dataLength ? bucketEnd : dataLength;
            for (int j = bucketStart;j<bucketEnd;j++){
                //和全部y值比较，如果有最大值或最小值，就使用
                if (data.get(j).y == theMax){
                    result.get(i).y = data.get(j).y;
                    continue;
                }else if (data.get(j).y == theMin){
                    result.get(i).y = data.get(j).y;
                    continue;
                }
                //如果有一个多次出现的y值，
            }

        }
        return result;
    }

    /**
     * 计算最大值
     * @param num
     * @return
     */
    private double getMax(double[] num){
        double max = num[0];
        for (int i = 0;i<num.length;i++){
            if (num[i]>max){
                max = num[i];
            }
        }
        return max;
    }
    /**
     * z计算最小值
     */
    private double getMin(double[] num){
        double min = num[0];
        for (int i = 0;i<num.length;i++){
            if (num[i]<min){
                min = num[i];
            }
        }
        return min;
    }
}
