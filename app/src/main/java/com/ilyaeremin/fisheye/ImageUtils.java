package com.ilyaeremin.fisheye;

/**
 * Created by ereminilya on 3/3/17.
 */

public class ImageUtils {

    public static int[] fisheye(int[] srcpixels, double w, double h) {
        // create the result data
        int[] dstpixels = new int[(int) (w * h)];
        for (int y = 0; y < h; y++) {
            double ny = ((2 * y) / h) - 1;
            double ny2 = ny * ny;
            for (int x = 0; x < w; x++) {
                double nx = ((2 * x) / w) - 1;
                double nx2 = nx * nx;
                double r = Math.sqrt(nx2 + ny2);
                if (0.0 <= r && r <= 1.0) {
                    double nr = Math.sqrt(1.0 - r * r);
                    nr = (r + (1.0 - nr)) / 2.0;
                    if (nr <= 1.0) {
                        // calculate the angle for polar coordinates
                        double theta = Math.atan2(ny, nx);
                        // calculate new x position with new distance in same angle
                        double nxn = nr * Math.cos(theta);
                        // calculate new y position with new distance in same angle
                        double nyn = nr * Math.sin(theta);
                        // map from -1 ... 1 to image coordinates
                        int x2 = (int) (((nxn + 1) * w) / 2.0);
                        // map from -1 ... 1 to image coordinates
                        int y2 = (int) (((nyn + 1) * h) / 2.0);
                        // find (x2,y2) position from source pixels
                        int srcpos = (int) (y2 * w + x2);
                        // make sure that position stays within arrays
                        if (srcpos >= 0 & srcpos < w * h) {
                            // get new pixel (x2,y2) and put it to target array at (x,y)
                            dstpixels[(int) (y * w + x)] = srcpixels[srcpos];
                        }
                    }
                }
            }
        }
        //return result pixels
        return dstpixels;
    }

    public static native int[] fisheyeNative(int[] srcPixels, int w, int h);

}
