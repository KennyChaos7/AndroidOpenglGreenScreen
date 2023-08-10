package com.hyq.hm.test.greenscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
//    private Camera2SurfaceView cameraView;
//    private ImageView coverIv;

//    private FrameLayout contentLayout;
    private Camera2GreenScreenLayout greenScreenLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (greenScreenLayout == null) {
            greenScreenLayout = findViewById(R.id.moving_layout);
        }
        greenScreenLayout.initCamera2TextureView(this, getWindowManager().getDefaultDisplay().getRotation());
    }



    public void onImage(View view){
    }
    public void onPoint(View view) {

    }

    public void onMove(View view) {
        greenScreenLayout.move(1);
    }

    private boolean isOpen = true;

    public void onOpenOrClose(View view) {
        if (isOpen) {
            isOpen = false;
            greenScreenLayout.removeAllViews();
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


}
