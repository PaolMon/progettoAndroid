package com.example.sensorsreader;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends Activity implements SensorEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    Sensor lgt;
    Sensor prs;

    TextView light;
    TextView presence;


    private static boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        light = findViewById(R.id.light);
        presence = findViewById(R.id.presence);


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lgt = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        prs = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorManager.registerListener(this, lgt, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, prs, SensorManager.SENSOR_DELAY_NORMAL);

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
                SensorsValues.setIlluminanceValue(event.values[0]);
                break;
            case Sensor.TYPE_PROXIMITY:
                presence.setText("presence sensor: " + event.values[0]);
                if (event.values[0]>0.5){
                    SensorsValues.updatePresence_counter();
                }
                SensorsValues.setPresence_state(event.values[0]>0.5);

                //LOGGER.info("presence: "+event.values[0]);
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