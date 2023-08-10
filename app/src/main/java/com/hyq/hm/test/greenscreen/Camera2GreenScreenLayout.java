package com.hyq.hm.test.greenscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;

public class Camera2GreenScreenLayout extends FrameLayout {

    private Camera2TextureView cameraView;
    private PointView pointView;

    public Camera2GreenScreenLayout(Context context) {
        super(context);
    }

    public Camera2GreenScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera2GreenScreenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Camera2GreenScreenLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initCamera2TextureView(Context context, int rotation) {
        if (cameraView == null && pointView == null) {
//            cameraView = new Camera2SurfaceView(this);
            cameraView = new Camera2TextureView(context);
            pointView = new PointView(context);
            cameraView.setSmooth(50f);
            pointView.setVisibility(View.INVISIBLE);
            addView(cameraView);
            addView(pointView);
            cameraView.init(
                    getBackgroundBitmap(cameraView),
                    pointView,
                    rotation);
        }
    }

    public void move(int witchDirection) {
        //1是往左边移动，2是往上边移动，3是往右边移动，4是往下边移动
        switch (witchDirection) {
            case 1: layout(getLeft() - 30, getTop(), getRight() - 30, getBottom());break;
            case 2: layout(getLeft(), getTop() - 30, getRight(), getBottom() - 30);break;
            case 3: layout(getLeft() + 30, getTop(), getRight() + 30, getBottom());break;
            case 4: layout(getLeft(), getTop() + 30, getRight(), getBottom() + 30);break;
        }
        isRect();
        pointView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap tmp = getBackgroundBitmap(cameraView);
                cameraView.setBitmap(tmp);
            }
        },100);
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        cameraView.stop();
        cameraView = null;
        pointView = null;
    }

    private Bitmap getBackgroundBitmap(View measureView) {
        View screenView = ((Activity)getContext()).getWindow().getDecorView();
        if (!screenView.isDrawingCacheEnabled())
            screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();

        if (bitmap != null) {
            //需要截取的长和宽 暂用正方形
            int outWidth = getWidth();
            int outHeight = getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            measureView.getLocationOnScreen(viewLocationArray);

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);

//            return bitmap;
            return adjustPhotoRotation(bitmap, Surface.ROTATION_90);
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

    private Bitmap adjustPhotoRotation(Bitmap bm, final int rotation) {
        Matrix matrix = new Matrix();

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            matrix.postRotate(90 * (rotation - 2),(float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        }else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        }

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, matrix, paint);

        return bm1;
    }
}
