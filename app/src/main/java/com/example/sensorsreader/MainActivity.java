package com.example.sensorsreader;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends Activity implements SensorEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    Sensor lgt;
    Sensor prx;
    Sensor stp;

    TextView light;
    TextView proximity;


    private static boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        light = findViewById(R.id.light);
        proximity = findViewById(R.id.proximity);


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lgt = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        prx = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorManager.registerListener(this, lgt, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, prx, SensorManager.SENSOR_DELAY_NORMAL);

        resume = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (resume) {
            resume = false;
            new Task().execute();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                light.setText("light Sensor: " + event.values[0]);
                SensorsValues.setTemperatureValue(event.values[0]);
                break;
            case Sensor.TYPE_PROXIMITY:
                proximity.setText("proximity sensor: " + event.values[0]);
                //LOGGER.info("proximity: "+event.values[0]);
                break;
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public class Task extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String[] a = {};
            Client.init(a, MainActivity.this);
            return null;
        }
    }
}