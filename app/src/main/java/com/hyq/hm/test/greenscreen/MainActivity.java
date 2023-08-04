package com.hyq.hm.test.greenscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Camera2SurfaceView cameraView;
    private PointView pointView;
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
//        cameraView = findViewById(R.id.camera_view);
//        pointView = findViewById(R.id.point_view);

        final FrameLayout layout = findViewById(R.id.layout_content);
        final ViewGroup motherBase = findViewById(R.id.mother_base_layout);
        cameraView = new Camera2SurfaceView(this);
        pointView = new PointView(this);
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.addView(cameraView);
                layout.addView(pointView);
                cameraView.init(getBackgroundBitmap(cameraView, layout, motherBase));
                isRect();
            }
        },200);
    }

    private Bitmap getBackgroundBitmap(View measureView, ViewGroup layout, ViewGroup motherBase) {
//        int width = imageView.getMeasuredWidth();
//        int height = imageView.getMeasuredHeight();
//        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bp);
//        imageView.draw(canvas);
//        canvas.save();

        View screenView = this.getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();
//        motherBase.setDrawingCacheEnabled(true);
//        motherBase.buildDrawingCache(true);

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();
//        Bitmap bitmap = motherBase.getDrawingCache();

        if (bitmap != null) {
            //需要截取的长和宽
            int outWidth = layout.getWidth();
            int outHeight = layout.getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            measureView.getLocationOnScreen(viewLocationArray);

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
            return bitmap;
        }
        return bitmap;
    }


    private void isRect(){
        pointView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rect = cameraView.getRect();
                if(rect.width() > 0 && rect.height() > 0){
                    pointView.setRect(rect);
                }else {
                    isRect();
                }

            }
        },100);
    }
    public void onImage(View view){
        cameraView.setCoordinate(pointView.getCoordinate(cameraView.getPreviewWidth(),cameraView.getPreviewHeight()),pointView.getRect());
    }
    public void onPoint(View view){
        Button button = (Button) view;
        if(pointView.getVisibility() == View.VISIBLE){
            pointView.setVisibility(View.INVISIBLE);

            button.setText("显示边框");
        }else{
            pointView.setVisibility(View.VISIBLE);
            button.setText("隐藏边框");
        }
    }
}
