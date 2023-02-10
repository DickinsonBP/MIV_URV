package com.example.pract2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MainActivity extends Activity {
private  static final float  TOUCH_SCALE_FACTOR= 0.07f;

    private GLSurfaceView glView;   // Use GLSurfaceView
    private MyGLRenderer myGLRenderer;
    private float previousX, previousY;
    //private boolean camera = false;
    private Paint paint;

    // Call back when the activity is started, to initialize the view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
        glView.setRenderer(myGLRenderer=new MyGLRenderer(this)); // Use a custom renderer
        this.setContentView(glView);                // This activity sets to GLSurfaceView
    }

    // Call back when the activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int newcamera = myGLRenderer.getCamera();

                //System.out.println("Camera: "+newcamera);

                newcamera = ++newcamera % 3;
                myGLRenderer.setCamera(newcamera);

        }

        previousX = x;
        previousY = y;
        return true;
    }

}