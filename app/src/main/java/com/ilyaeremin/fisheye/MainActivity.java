package com.ilyaeremin.fisheye;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.nio.IntBuffer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView uiImage = (ImageView) findViewById(R.id.image);
        findViewById(R.id.apply_fisheye).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) uiImage.getDrawable()).getBitmap();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                int[] fisheye = ImageUtils.fisheyeNative(pixels, width, height);
                Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                // vector is your int[] of ARGB
                output.copyPixelsFromBuffer(IntBuffer.wrap(fisheye));
                uiImage.setImageBitmap(output);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}