package com.example.sensorsreader;

public class SensorsValues {
    static float temperature_value = 0;

    static void setTemperatureValue(float x) {
        temperature_value = x;
    }

    static float getTemperatureValue() {
        return temperature_value;
    }
}
