package com.example.sensorsreader;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    Sensor lgt;
    Sensor prx;
    Sensor stp;

    TextView light;
    TextView proximity;
    TextView steps;
    TextView otherSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        light = findViewById(R.id.light);
        proximity = findViewById(R.id.proximity);
        steps = findViewById(R.id.steps);
        otherSensors = findViewById(R.id.otherSensors);
        otherSensors.setVisibility(View.GONE);


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        lgt = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        prx = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        stp = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        sensorManager.registerListener(this, lgt, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, prx, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stp, SensorManager.SENSOR_DELAY_NORMAL);

        List<Sensor> mList= sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 1; i < mList.size(); i++) {
            otherSensors.setVisibility(View.VISIBLE);
            otherSensors.append("\nNAME: " + mList.get(i).getName() + "\nTYPE: " + mList.get(i).getStringType() + "\nVENDOR: " + mList.get(i).getVendor() + "\nVERSION: " + mList.get(i).getVersion());
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                light.setText("light Sensor: " + event.values[0]);
                System.out.println(event.values);
                break;
            case Sensor.TYPE_PROXIMITY:
                proximity.setText("proximity sensor: " + event.values[0]);
                System.out.println(event.values);
                break;
            case Sensor.TYPE_STEP_COUNTER:
                steps.setText("steps: " + event.values[0]);
                System.out.println(event.values);
                break;
            default:
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}