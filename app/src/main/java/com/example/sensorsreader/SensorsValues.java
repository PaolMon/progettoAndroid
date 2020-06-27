package com.example.sensorsreader;

public class SensorsValues {
    static float illuminance_value = 0;
    static boolean presence_state= false;
    static int presence_counter =0;

    static void setIlluminanceValue(float x) {
        SensorsValues.illuminance_value = x;
    }

    static float getIlluminanceValue() {
        return SensorsValues.illuminance_value;
    }

    public static boolean isPresence_state() {
        return presence_state;
    }

    public static void setPresence_state(boolean presence_state) {
        SensorsValues.presence_state = presence_state;
    }

    public static int getPresence_counter() {
        return presence_counter;
    }

    public static void updatePresence_counter() {
        if(!SensorsValues.presence_state) {
            SensorsValues.presence_counter +=1;
        }
    }

    public static void resetPresence_counter() {
        SensorsValues.presence_counter = 0;
    }
}
