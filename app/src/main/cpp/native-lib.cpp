#include <jni.h>
#include <string>
#include <math.h>

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_ilyaeremin_fisheye_ImageUtils_fisheyeNative(JNIEnv *env,
                                                     jclass type,
                                                     jintArray srcPixels_,
                                                     jint w, jint h) {
    jint *srcPixels = env->GetIntArrayElements(srcPixels_, NULL);
    jintArray outPixels_ = env->NewIntArray(w * h);
    if (outPixels_ == NULL) {
        return NULL;
    }
    jint *outPixels = env->GetIntArrayElements(outPixels_, NULL);

    for (int y = 0; y < h; ++y) {
        double ny = ((2 * y) / h) - 1;
        double ny2 = ny * ny;
        for (int x = 0; x < w; ++x) {
            double nx = ((2 * x) / w) - 1;
            double nx2 = nx * nx;
            double r = sqrt(nx2 + ny2);

            if (0.0 <= r && r <= 1.0) {
                double nr = sqrt(1.0 - r * r);
                nr = (r + (1.0 - nr)) / 2.0;
                if (nr <= 1.0) {
                    double theta = atan2(ny, nx);
                    double nxn = nr * cos(theta);
                    double nyn = nr * sin(theta);
                    int x2 = (int) (((nxn + 1) * w) / 2.0);
                    int y2 = (int) (((nyn + 1) * h) / 2.0);
                    int srcpos = y2 * w + x2;
                    if (srcpos >= 0 & srcpos < w * h) {
                        outPixels[y * w + x] = srcPixels[srcpos];
                    }
                }
            }

        }
    }
    env->ReleaseIntArrayElements(srcPixels_, srcPixels, 0);
    env->ReleaseIntArrayElements(outPixels_, outPixels, 0);
    return outPixels_;
}

extern "C"
jstring
Java_com_ilyaeremin_fisheye_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
