package com.hyq.hm.test.greenscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GlBackgroudSurfaceView extends SurfaceView {


    public GlBackgroudSurfaceView(Context context) {
        super(context);
        init();
    }

    public GlBackgroudSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlBackgroudSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GlBackgroudSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        setZOrderOnTop(false);
        setZOrderMediaOverlay(false);
        super.onAttachedToWindow();
    }

    private void init(){
        getHolder().setFormat(PixelFormat.OPAQUE);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Canvas canvas = holder.lockCanvas();
                try {
                    canvas.drawColor(Color.RED);
                } finally {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
