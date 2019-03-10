package ir.hphamid.cps_ca3;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.abs;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener{
    private boolean started = false;
    private Sensor sensor;
    private long last_event_time = 0;
    private double xPos, xw, xVel = 0.0f;
    private double yPos, yw, yVel = 0.0f;
    private double xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;
    private int m = 10;
    private double us = 0.15;
    private double uk = 0.10;
    private double zw = 0.0f;
    private float currentxDegree = 0;
    private float currentyDegree = 0;
    private float currentzDegree = 0;
    private double G = 9.8;

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.cool_pic);
            final int dstWidth = 100;
            final int dstHeight = 100;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(ball, (float) xPos, (float) yPos, null);
            invalidate();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BallView ballView = new BallView(this);
        setContentView(ballView);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y - 400;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!started){
            yPos = yMax / 2;
            xPos = xMax / 2;
            started = true;
        }
        return super.onTouchEvent(event);
    }



    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float frameTime = .266f;
        if (!started)
            return;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            zw = sensorEvent.values[2];
            yw = -sensorEvent.values[0];
            xw = -sensorEvent.values[1];

            currentxDegree += xw * frameTime;
            currentyDegree += yw * frameTime;
            currentzDegree += zw * frameTime;
////
//            LinearLayout lView = new LinearLayout(this);
//            TextView mytext = new TextView(this);
//            mytext.setText(String.valueOf(currentxDegree) + '\n' + String.valueOf(currentyDegree) + '\n' + String.valueOf(currentzDegree));
//            lView.addView(mytext);
//            setContentView(lView);

            xw = sin(currentxDegree) * G;
            yw = sin(currentyDegree) * G;
            zw = cos(currentzDegree) * G;


//            LinearLayout lView = new LinearLayout(this);
//            TextView mytext = new TextView(this);
//            mytext.setText(String.valueOf(xw) + '\n' + String.valueOf(yw) + '\n' + String.valueOf(zw));
//            lView.addView(mytext);
//            setContentView(lView);


            if (abs(xw) < abs(zw * us) && xVel == 0)
                xw = 0;
            else
                    xw += zw * uk;

            if (abs(yw) < abs(zw * us) && yVel == 0)
                yw = 0;
            else
                    yw += zw * uk;

            LinearLayout lView = new LinearLayout(this);
            TextView mytext = new TextView(this);
            mytext.setText(String.valueOf(xw) + '\n' + String.valueOf(yw) + '\n' + String.valueOf(zw));
            lView.addView(mytext);
            setContentView(lView);

            updateBall();
        }
    }

    private void updateBall() {
        float frameTime = .266f;
        xVel += (xw * frameTime);
        yVel += (yw * frameTime);

        double xS = (xVel / 2) * frameTime;
        double yS = (yVel / 2) * frameTime;

        xPos -= xS;
        yPos -= yS;

        if (xPos > xMax) {
            xPos = xMax;
            xVel = 0;
        } else if (xPos < 0) {
            xPos = 0;
            xVel = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
            yVel = 0;
        } else if (yPos < 0) {
            yPos = 0;
            yVel = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}