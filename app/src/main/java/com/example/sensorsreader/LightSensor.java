package com.example.sensorsreader;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.ReadResponse;


public class LightSensor extends BaseInstanceEnabler {

    static final int SENSOR_VALUE = 5700;
    static final int MIN_MEASURED_VALUE = 5601;
    static final int MAX_MEASURED_VALUE = 5602;
    static final int RESET_MIN_AND_MAX_MEASURED_VALUES = 5605;
    static final int SENSOR_UNIT = 5701;

    float actual_value = 1;
    float max_value = 0;
    float min_value = 100000;



    public LightSensor() {

    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        switch (resourceid) {
            case SENSOR_VALUE:
                setSensorValue(SensorsValues.getIlluminanceValue());
                return ReadResponse.success(resourceid, actual_value);
            case MIN_MEASURED_VALUE:
                return ReadResponse.success(resourceid, min_value);
            case MAX_MEASURED_VALUE:
                return ReadResponse.success(resourceid, max_value);
        }
        return ReadResponse.notFound();
    }

    public void setSensorValue(float x) {
        actual_value = x;
        if(actual_value > max_value) {
            max_value = actual_value;
        }
        if(actual_value < min_value) {
            min_value = actual_value;
        }
    }

    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {
        switch (resourceid) {
            case RESET_MIN_AND_MAX_MEASURED_VALUES:
                max_value = 0;
                min_value = 100000;
                return ExecuteResponse.success();
        }
        return ExecuteResponse.notFound();
    }

    @Override
    public ObserveResponse observe(ServerIdentity identity, int resourceid) {
        // Perform a read by default
        ReadResponse readResponse = this.read(identity, resourceid);
        return new ObserveResponse(readResponse.getCode(), readResponse.getContent(), null, null,
                readResponse.getErrorMessage());
    }
}
