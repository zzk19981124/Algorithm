
package com.example.algorithm.Helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GeoHelper {
    /*
     * 大地坐标系资料WGS-84 长半径a=6378137 短半径b=6356752.3142 扁率f=1/298.2572236
     */
    /** 长半径a=6378137 */
    //private double a = 6378137;
    /** 短半径b=6356752.3142 */
    //private double b = 6356752.3142;
    /**
     * 扁率f=1/298.2572236
     */
    //private double f = 1 / 298.2572236;

    //Radius of WGS84 sphere
    private static double RADIUS = 6378137;
    private static double RADIUS_B = 6356752.314245;
    private static double E = (RADIUS * RADIUS - RADIUS_B * RADIUS_B) / (RADIUS * RADIUS);
    private static double HALF_SIZE = Math.PI * RADIUS;
    private static double DEG2RAD = Math.PI / 180;
    private static double RAD2DEG = 180 / Math.PI;
    private static double RE_WGS84 = 6378137.0;
    private static double FE_WGS84 = (1.0 / 298.257223563);

    public static boolean isFirstPoint = true;
    private static double[] bPos = new double[3];
    private static double[] bECEF = new double[3];
    private static double[] rPos = new double[3];
    private static double[] rECEF = new double[3];
    private static double[] vECEF = new double[3];

    /**
     * WGS84坐标转ENU 。经纬高
     */
    public static Pt WGS84ToENU(double lon, double lat, double alt) {
        if (isFirstPoint) {
            setEnuBenchmark(lon, lat, alt);
        }
        rPos[0] = lat * DEG2RAD;
        rPos[1] = lon * DEG2RAD;
        rPos[2] = alt;

        rECEF = pos2ecef(rPos);
        vECEF[0] = rECEF[0] - bECEF[0];
        vECEF[1] = rECEF[1] - bECEF[1];
        vECEF[2] = rECEF[2] - bECEF[2];
        double[] enuDoubleArray = ecef2enu(bPos, vECEF);
        Pt enu = new Pt();
        enu.x = enuDoubleArray[0];
        enu.y = enuDoubleArray[1];
        enu.z = enuDoubleArray[2];
        return enu;
    }

    /** Set the reference point of ENU coordinate system */
    public static void setEnuBenchmark(double lon, double lat, double alt) {
        bPos[0] = lat * DEG2RAD;
        bPos[1] = lon * DEG2RAD;
        bPos[2] = alt;
        bECEF = pos2ecef(bPos);
        isFirstPoint = false;
    }

    /**
     * WGS84坐标转EPSG3857
     */
    public static Pt WGS84ToEPSG3857(double lon, double lat) {
        double x = lon * HALF_SIZE / 180;
        double y = RADIUS * Math.log(Math.tan(Math.PI * (lat + 90) / 360));
        if (y > HALF_SIZE) {
            y = HALF_SIZE;
        } else if (y < -HALF_SIZE) {
            y = -HALF_SIZE;
        }

        return new Pt(x, y);
    }

    /** transform geodetic to ecef position -----------------------------------------
     * transform geodetic position to ecef position
     * args   : double *pos      I   geodetic position {lat,lon,h} (rad,m)
     *          double *r        O   ecef position {x,y,z} (m)
     * return : none
     * notes  : WGS84, ellipsoidal height
     *-----------------------------------------------------------------------------*/
    private static double[] pos2ecef(double[] pos) {
        double[] r = new double[3];
        double sinp = Math.sin(pos[0]), cosp = Math.cos(pos[0]), sin_l = Math.sin(pos[1]), cos_l = Math.cos(pos[1]);
        double e2 = FE_WGS84 * (2.0 - FE_WGS84), v = RE_WGS84 / Math.sqrt(1.0 - e2 * sinp * sinp);

        r[0] = (v + pos[2]) * cosp * cos_l;
        r[1] = (v + pos[2]) * cosp * sin_l;
        r[2] = (v * (1.0 - e2) + pos[2]) * sinp;
        return r;
    }

    /** transform ecef vector to local tangental coordinate -------------------------
     * transform ecef vector to local tangental coordinate
     * args   : double *pos      I   geodetic position {lat,lon} (rad)
     *          double *r        I   vector in ecef coordinate {x,y,z}
     *          double *e        O   vector in local tangental coordinate {e,n,u}
     * return : none
     *-----------------------------------------------------------------------------*/
    //#pragma CODE_SECTION(ecef2enu,"sect_ECODE_I");
    private static double[] ecef2enu(double[] pos, double[] r) {
        double E[];

        E = xyz2enu(pos);

        return matmul("NN".toCharArray(), 3, 1, 3, 1.0, E, r, 0.0);
    }

    /**
     * multiply matrix (wrapper of blas dgemm) -------------------------------------
     * multiply matrix by matrix (C=alpha*A*B+beta*C)
     * args   : char   *tr       I  transpose flags ("N":normal,"T":transpose)
     * int    n,k,m     I  size of (transposed) matrix A,B
     * double alpha     I  alpha
     * double *A,*B     I  (transposed) matrix A (n x m), B (m x k)
     * double beta      I  beta
     * double *C        IO matrix C (n x k)
     * return : none
     * -----------------------------------------------------------------------------
     */
    private static double[] matmul(char[] tr, int n, int k, int m, double alpha,
                                   double[] A, double[] B, double beta) {
        double d;
        double[] C = new double[n * k];
        int i, j, x, f = tr[0] == 'N' ? (tr[1] == 'N' ? 1 : 2) : (tr[1] == 'N' ? 3 : 4);
        for (i = 0; i < n; i++) {
            for (j = 0; j < k; j++) {
                d = 0.0;
                switch (f) {
                    case 1:
                        for (x = 0; x < m; x++) {
                            d += A[i + x * n] * B[x + j * m];
                        }
                        break;
                    case 2:
                        for (x = 0; x < m; x++) {
                            d += A[i + x * n] * B[j + x * k];
                        }
                        break;
                    case 3:
                        for (x = 0; x < m; x++) {
                            d += A[x + i * m] * B[x + j * m];
                        }
                        break;
                    case 4:
                        for (x = 0; x < m; x++) {
                            d += A[x + i * m] * B[j + x * k];
                        }
                        break;
                    default:
                        break;
                }
                if (beta == 0.0) {
                    C[i + j * n] = alpha * d;
                } else {
                    C[i + j * n] = alpha * d + beta * C[i + j * n];
                }
            }
        }
        return C;
    }

    private static double[] xyz2enu(double[] pos) {
        double[] E = new double[9];
        double sinp = Math.sin(pos[0]), cosp = Math.cos(pos[0]), sin_l = Math.sin(pos[1]), cos_l = Math.cos(pos[1]);
        //double sinp,cosp,sinl,cosl;

        E[0] = -sin_l;
        E[3] = cos_l;
        E[6] = 0.0;
        E[1] = -sinp * cos_l;
        E[4] = -sinp * sin_l;
        E[7] = cosp;
        E[2] = cosp * cos_l;
        E[5] = cosp * sin_l;
        E[8] = sinp;
        return E;
    }

    //地心坐标系 = EPSG4987 = ECEF
    public static Pt ECEF_FromWGS84(double lon, double lat, double alt) {
        double rLat = lat * DEG2RAD;
        double rLon = lon * DEG2RAD;
//
//        double COSLAT = Math.cos(rLat);
//        double SINLAT = Math.sin(rLat);
//        double COSLONG = Math.cos(rLon);
//        double SINLONG = Math.sin(rLon);
//
//        double N = RADIUS /(Math.sqrt(1 - E * SINLAT*SINLAT));
//        double NH = N + alt;
//
//        double X = NH * COSLAT * COSLONG;
//        double Y = NH * COSLAT * SINLONG;
////        Z = (b*b*N/(RADIUS*RADIUS) + height) * SINLAT;
//
        double sinLat = Math.sin(rLat);
        double cosLat = Math.cos(rLat);
        double sinLon = Math.sin(rLon);
        double cosLon = Math.cos(rLon);
        double e2 = FE_WGS84 * (2.0 - FE_WGS84);
        double N = RE_WGS84 / Math.sqrt(1.0 - e2 * sinLat * sinLat);

        double X = (N + alt) * cosLat * cosLon;
        double Y = (N + alt) * cosLat * sinLon;
        double Z = (N * (1 - e2) + alt) * sinLat;

//        double r[2] = (v * (1.0 - e2) + alt) * sinp;
        return new Pt(X, Y, Z);
    }

    /**
     * WGS84转成Enu
     *
     * @param lon
     * @param lat
     * @param alt
     * @return
     */
    public static Pt Enu_FromWGS84(double lon, double lat, double alt) {
        return new Pt(MapProLatLonUtils.latLon2Enu(lon, lat, alt));
    }

    /**
     * Enu转成WGS84
     *
     * @param pt
     * @return
     */
    public static double[] WGS84FromEnu(Pt pt) {
        return MapProLatLonUtils.xy2LatLon(pt.x, pt.y);
    }

    public static void setIsEnuFirstPoint(boolean isEnuFirstPoint) {
        MapProLatLonUtils.isFirstPoint = isEnuFirstPoint;
    }

    /**
     * 两点的距离 在赤道附近从正到负计算错误
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     * @deprecated
     */
    public static double LantitudeLongitudeDist(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = lat1 * DEG2RAD;
        double radLat2 = lat2 * DEG2RAD;

        double radLon1 = lon1 * DEG2RAD;
        double radLon2 = lon2 * DEG2RAD;

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = RADIUS * Math.cos(radLat1);

        double x2 = RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((RADIUS * RADIUS + RADIUS * RADIUS - d * d) / (2 * RADIUS * RADIUS));
        double dist = theta * RADIUS;
        return dist;
    }

    public static class Pt implements Parcelable {
        public double x;
        public double y;
        public double z;

        public Pt() {
        }

        public Pt(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Pt(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Pt(double[] pt) {
            if (pt.length == 2) {
                this.x = pt[0];
                this.y = pt[1];
            } else if (pt.length == 3) {
                this.x = pt[0];
                this.y = pt[1];
                this.z = pt[2];
            }
        }

        protected Pt(Parcel in) {
            x = in.readDouble();
            y = in.readDouble();
            z = in.readDouble();
        }

        public static final Creator<Pt> CREATOR = new Creator<Pt>() {
            @Override
            public Pt createFromParcel(Parcel in) {
                return new Pt(in);
            }

            @Override
            public Pt[] newArray(int size) {
                return new Pt[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(x);
            parcel.writeDouble(y);
            parcel.writeDouble(z);
        }

        @Override
        public String toString() {
            return "Pt{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    //道格拉斯-普克算法
    public static List<Pt> douglasPeucker(List<Pt> flatCoordinates, double squaredTolerance) {
        ArrayList<Pt> simplifiedFlatCoordinates = new ArrayList<>();

        int n = flatCoordinates.size();
        if (n < 3) {
            return flatCoordinates;
        }

        int[] markers = new int[n];
        Arrays.fill(markers, 0);
        markers[0] = 1;
        markers[n - 1] = 1;

        Stack<Integer> stack = new Stack<>();
        int index = 0;
        stack.add(0);
        stack.add(n - 1);

        while (stack.size() > 0) {
            int last = stack.pop();
            int first = stack.pop();
            double maxSquaredDistance = 0;
            Pt p1 = flatCoordinates.get(first);
            double x1 = p1.x;
            double y1 = p1.y;
            Pt p2 = flatCoordinates.get(last);
            double x2 = p2.x;
            double y2 = p2.y;

            for (int i = first + 1; i < last; i++) {
                Pt pp = flatCoordinates.get(i);
                double x = pp.x;
                double y = pp.y;
                double squaredDistance = squaredSegmentDistance(x, y, x1, y1, x2, y2);
                if (squaredDistance > maxSquaredDistance) {
                    index = i;
                    maxSquaredDistance = squaredDistance;
                }
            }

            if (maxSquaredDistance > squaredTolerance) {
                markers[index] = 1;
                if (first + 1 < index) {
                    //stack.push(first, index);
                    stack.push(first);
                    stack.push(index);
                }
                if (index + 1 < last) {
                    //stack.push(index, last);
                    stack.push(index);
                    stack.push(last);
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            if (markers[i] != 0) {
                simplifiedFlatCoordinates.add(flatCoordinates.get(i));
            }
        }

        return simplifiedFlatCoordinates;
    }

    public static double squaredSegmentDistance(double x, double y, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
//        long zero = Double.doubleToLongBits(0);
        if (Double.doubleToLongBits(dx) != 0 || Double.doubleToLongBits(dy) != 0) {
            double t = ((x - x1) * dx + (y - y1) * dy) / (dx * dx + dy * dy);
            if (t > 1) {
                x1 = x2;
                y1 = y2;
            } else if (t > 0) {
                x1 += dx * t;
                y1 += dy * t;
            }
        }
        return squaredDistance(x, y, x1, y1);
    }

    public static double squaredDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(squaredDistance(x1, y1, x2, y2));
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(squaredDistance(x1, y1, z1, x2, y2, z2));
    }

    public static double crossProduct(double x1, double y1, double x2, double y2) {
        return y1 * x2 - x1 * y2;
    }
}
