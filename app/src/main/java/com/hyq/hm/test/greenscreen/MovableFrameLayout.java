package com.hyq.hm.test.greenscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MovableFrameLayout extends FrameLayout {
    public MovableFrameLayout(Context context) {
        super(context);
    }

    public MovableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MovableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void move(int witchDirection, View cameraView, Activity context) {
        //1是往左边移动，2是往上边移动，3是往右边移动，4是往下边移动
        switch (witchDirection) {
            case 1: layout(getLeft() - 30, getTop(), getRight() - 30, getBottom());break;
            case 2: layout(getLeft(), getTop() - 30, getRight(), getBottom() - 30);break;
            case 3: layout(getLeft() + 30, getTop(), getRight() + 30, getBottom());break;
            case 4: layout(getLeft(), getTop() + 30, getRight(), getBottom() + 30);break;
        }
//        cameraView.setBitmap(getBackgroundBitmap(cameraView, this, context));
    }
    private Bitmap getBackgroundBitmap(View measureView, ViewGroup layout, Activity context) {
//        int width = imageView.getMeasuredWidth();
//        int height = imageView.getMeasuredHeight();
//        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bp);
//        imageView.draw(canvas);
//        canvas.save();

        View screenView = context.getWindow().getDecorView();
        if (!screenView.isDrawingCacheEnabled())
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
}
