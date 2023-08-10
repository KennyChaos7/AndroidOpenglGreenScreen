package com.hyq.hm.test.greenscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
//    private Camera2SurfaceView cameraView;
    private Camera2TextureView cameraView;
    private PointView pointView;
//    private ImageView coverIv;

//    private FrameLayout contentLayout;
    private MovableFrameLayout movableFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text_view);
        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int p = progress+5;
                textView.setText(p+"");
                cameraView.setSmooth(p);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean issssss = false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!issssss)
            initCamera2ViewGreenScreen();
        issssss = true;
    }

    private void initCamera2ViewGreenScreen(){
        if (movableFrameLayout == null)
            movableFrameLayout = findViewById(R.id.moving_layout);
        if (cameraView == null && pointView == null) {
//            cameraView = new Camera2SurfaceView(this);
            cameraView = new Camera2TextureView(this);
            pointView = new PointView(this);
            pointView.setVisibility(View.INVISIBLE);
            movableFrameLayout.addView(cameraView);
            movableFrameLayout.addView(pointView);
            cameraView.init(getBackgroundBitmap(
                    cameraView, movableFrameLayout),
                    pointView,
                    getWindowManager().getDefaultDisplay().getRotation());
//            pointView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    isRect();
//                }
//            }, 200);
//            movableFrameLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    cameraView.setCoordinate(pointView.getCoordinate(cameraView.getPreviewWidth(), cameraView.getPreviewHeight()), pointView.getRect());
//                }
//            }, 200);
        }
    }

    private Bitmap getBackgroundBitmap(View measureView, ViewGroup layout) {
        View screenView = this.getWindow().getDecorView();
        if (!screenView.isDrawingCacheEnabled())
            screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();

        if (bitmap != null) {
            //需要截取的长和宽
            int outWidth = layout.getWidth();
            int outHeight = layout.getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            measureView.getLocationOnScreen(viewLocationArray);

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);

//            return bitmap;
            return adjustPhotoRotation(bitmap, -90);
        }
        return bitmap;
    }


    private void isRect(){
        Rect rect = cameraView.getRect();
        if(rect.width() > 0 && rect.height() > 0){
            pointView.setRect(rect);
        }else {
            isRect();
        }
    }
    public void onImage(View view){
        cameraView.setCoordinate(pointView.getCoordinate(cameraView.getPreviewWidth(),cameraView.getPreviewHeight()),pointView.getRect());
    }
    public void onPoint(View view) {
        Button button = (Button) view;
        if (pointView.getVisibility() == View.VISIBLE) {
            pointView.setVisibility(View.INVISIBLE);
            button.setText("显示边框");
        } else {
            pointView.setVisibility(View.VISIBLE);
            button.setText("隐藏边框");
        }
    }

    public void onMove(View view) {
        movableFrameLayout.move(1, cameraView, this);
        isRect();
        pointView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap tmp = getBackgroundBitmap(cameraView, movableFrameLayout);
//                saveImageToGallery(tmp);
                cameraView.setBitmap(tmp);
            }
        },100);
    }

    private boolean isOpen = true;

    public void onOpenOrClose(View view) {
        if (isOpen) {
            isOpen = false;
            cameraView.stop();
            movableFrameLayout.removeAllViews();
            cameraView = null;
            pointView = null;
        }
        else {
            initCamera2ViewGreenScreen();
            isOpen = true;
        }
    }

    private int saveImageToGallery(Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "erweima16";
        File appDir = new File(root , dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            Log.e(this.getClass().getSimpleName(), file.getAbsolutePath());
            return 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//        float targetX, targetY;
//        if (orientationDegree == 90) {
//            targetX = bm.getHeight();
//            targetY = 0;
//        } else {
//            targetX = bm.getHeight();
//            targetY = bm.getWidth();
//        }
//
//        final float[] values = new float[9];
//        m.getValues(values);
//
//        float x1 = values[Matrix.MTRANS_X];
//        float y1 = values[Matrix.MTRANS_Y];
//
//        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);


        return bm1;
    }
}
