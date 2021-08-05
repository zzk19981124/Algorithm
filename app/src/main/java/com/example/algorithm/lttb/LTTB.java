package com.example.algorithm.lttb;

import com.example.algorithm.Helper.GeoHelper;

import java.util.ArrayList;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/23
 */
public class LTTB {

    public static ArrayList<GeoHelper.Pt> getLTTB(ArrayList<GeoHelper.Pt> data, int threshold) {
        int dataLength = data.size();
        if (threshold >= dataLength || threshold == 0) return data;
        ArrayList<GeoHelper.Pt> reducedData = new ArrayList<>(threshold);
        double everyBucket = (dataLength - 2) / (threshold - 2);//点集大小
        int a = 0;
        int nextA = 0;
        GeoHelper.Pt maxAreaPoint = new GeoHelper.Pt();
        reducedData.add(data.get(a)); //默认添加首点
        //将数据分成与阈值相等数量的桶，但第一个桶只包含第一个数据点，最后一个桶只包含最后一个数据点
        for (int i = 0; i < threshold - 2; i++) {
            //当前采样的是第二个区间，计算第三个区间的平均数
            double avgX = 0;
            double avgY = 0;
            int avgRangeStart = (int) (((i + 1) * everyBucket) + 1);
            int avgRangeEnd = (int) (((i + 2) * everyBucket) + 1);
            avgRangeEnd = avgRangeEnd < dataLength ? avgRangeEnd : dataLength;

            int avgRangeLength = avgRangeEnd - avgRangeStart;
            for (; avgRangeStart < avgRangeEnd; avgRangeStart++) {
                avgX += data.get(avgRangeStart).x;
                avgY += data.get(avgRangeEnd).y;
            }
            avgX /= avgRangeLength;
            avgY /= avgRangeLength;
            //点A
            double pointAx = data.get(a).x;
            double pointAy = data.get(a).y;

            double maxArea = -1;
            //+1是为了排除当前点
            int rangeOffs = (int) (Math.floor((i + 0) * everyBucket) + 1);
            int rangeTo = (int) (Math.floor((i + 1) * everyBucket) + 1);
            for (; rangeOffs < rangeTo; rangeOffs++) {
                //计算面积 点1  点集2  虚拟的点3
                //s = ((x3 - x1)(y3 - y1) - (x3 - x2)(y3 - y2))*0.5
                double area = Math.abs((pointAx - avgX) * (data.get(rangeOffs).y - pointAy) -
                        (pointAx - data.get(rangeOffs).x) * (avgY - pointAy)) * 0.5;
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaPoint = data.get(rangeOffs);
                    nextA = rangeOffs; //设置下一个点
                }
            }
            reducedData.add(maxAreaPoint);
            a = nextA; // 进入下一次循环
        }
        reducedData.add(data.get(dataLength - 1));//默认添加尾节点
        return reducedData;
    }
}
