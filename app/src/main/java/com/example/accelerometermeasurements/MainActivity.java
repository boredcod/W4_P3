package com.example.accelerometermeasurements;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SeekBar simpleSeekBar; // initiate the Seek bar
    int seekBarValue;
    GestureDetectorCompat gestureDetectorCompat;
    WebView myWebView;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        simpleSeekBar = findViewById(R.id.simpleSeekBar);
        myWebView = findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = i;

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String t = String.valueOf(seekBarValue*100);
                Toast.makeText(getApplicationContext(),t,Toast.LENGTH_SHORT).show();
            }
        });
        gestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float angle_d = (float) Math.toDegrees(Math.atan2(e1.getY() - e2.getY(), e2.getX() - e1.getX()));
                float angle = Math.abs(angle_d);
                float speed = (Math.abs(velocityX) + Math.abs(velocityY));
                float comp = seekBarValue * 100;
                System.out.println(seekBarValue);
                if (speed > comp) {
                    if (angle < 30 && angle < 90 || angle > 90 && angle > 150) {
                        Log.d("hi", "X-direction");
                        myWebView.loadUrl("https://www.ecosia.org/");
                        return true;
                    }
                    else if (angle < 60 && angle < 90 || angle > 90 && angle > 120) {
                        Log.d("hi", "Z-direction");
                        myWebView.loadUrl("https://webb.nasa.gov/");
                        return true;
                    }
                    else {
                        Log.d("hi", "Y-direction");
                        myWebView.loadUrl("https://www.dogpile.com/");
                        return true;
                    }
                }
                return false;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float axisX = sensorEvent.values[0];
            float axisY = sensorEvent.values[1];
            float axisZ = sensorEvent.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ + gravityZ;
            double squareD = Math.sqrt(f.doubleValue());
            float gForce = (float) squareD;
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                long currentTime = System.currentTimeMillis();
                if (mShakeTime + SHAKE_SKIP_TIME > currentTime) {
                    return;
                }
                mShakeTime = currentTime;
                myWebView.loadUrl("https://jumpingjaxfitness.files.wordpress.com/2010/07/dizziness.jpg");

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}