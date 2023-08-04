package com.hyq.hm.test.greenscreen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
@Deprecated
public class Main22222222Activity extends AppCompatActivity {
    private Camera2SurfaceView cameraView;
    private PointView pointView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
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
        initCamera2ViewGreenScreen();
    }

    private void initCamera2ViewGreenScreen(){
        final FrameLayout layout = findViewById(R.id.layout_content);
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                SurfaceView bgView = findViewById(R.id.bg_view);
                cameraView = findViewById(R.id.camera_view);
                pointView = findViewById(R.id.point_view);

                layout.removeAllViews();
                bgView.setZOrderMediaOverlay(false);
                bgView.setZOrderOnTop(false);
                cameraView.setZOrderMediaOverlay(true);
                cameraView.setZOrderOnTop(true);
                layout.addView(bgView);
                layout.addView(cameraView);
                layout.addView(pointView);

                isRect();
            }
        },200);
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
