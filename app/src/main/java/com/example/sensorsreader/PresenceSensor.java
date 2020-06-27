package com.example.sensorsreader;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;


public class PresenceSensor extends BaseInstanceEnabler {
    static final int DIGITAL_INPUT_STATE= 5500;
    static final int DIGITAL_INPUT_COUNTER= 5501;
    static final int DIGITAL_INPUT_COUNTER_RESET=5505;

    public PresenceSensor() {
    }

    @Override
    public ReadResponse read(ServerIdentity identity, int resourceid) {
        switch (resourceid) {
            case DIGITAL_INPUT_STATE:
                return ReadResponse.success(resourceid, SensorsValues.isPresence_state());
            case DIGITAL_INPUT_COUNTER:
                return ReadResponse.success(resourceid, SensorsValues.getPresence_counter());
        }
        return ReadResponse.notFound();
    }

    @Override
    public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {
        switch (resourceid) {
            case DIGITAL_INPUT_COUNTER_RESET:
                SensorsValues.resetPresence_counter();
                return ExecuteResponse.success();
        }
        return ExecuteResponse.notFound();
    }


}
