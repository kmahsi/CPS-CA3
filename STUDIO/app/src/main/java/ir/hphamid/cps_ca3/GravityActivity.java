package ir.hphamid.cps_ca3;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import static java.lang.StrictMath.abs;

public class GravityActivity extends AppCompatActivity implements SensorEventListener{
    private boolean started = false;
    private Sensor sensor;
    private long last_event_time = 0;
    private double xPos, xAccel, xVel = 0.0f;
    private double yPos, yAccel, yVel = 0.0f;
    private double xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;
    private int m = 10;
    private double us = 0.15;
    private double uk = 0.10;
    private float zAccel = 0.0f;


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
        xMax = (float) size.x - 300;
        yMax = (float) size.y - 400;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (started != true){
            yPos = yMax / 2;
            xPos = xMax / 2;
            started = true;
        }
        return super.onTouchEvent(event);
    }



    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (!started)
            return;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {

            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            zAccel = -sensorEvent.values[2];
            if (abs(xAccel * m) < abs(zAccel * m * us) && xVel == 0)
                xAccel = 0;
            else
                xAccel -= zAccel * uk;
            if (abs(yAccel * m) < abs(zAccel * m * us) && yVel == 0)
                yAccel = 0;
            else
                yAccel -= zAccel * uk;
            updateBall();
        }
    }

    private void updateBall() {
        float frameTime = .266f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        double xS = (xVel / 2) * frameTime;
        double yS = (yVel / 2) * frameTime;

        xPos -= xS;
        yPos -= yS;

        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
        } else if (yPos < 0) {
            yPos = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}