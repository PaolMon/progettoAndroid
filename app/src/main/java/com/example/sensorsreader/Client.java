package com.example.sensorsreader;

import android.content.Context;
import android.os.Build;

import org.eclipse.leshan.core.LwM2m;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.StaticModel;
import org.eclipse.leshan.core.request.BindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.leshan.core.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

public class Client {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    static final int ILLUMINANCE = 3301;
    static final String ILLUMINANCE_MODEL = "3301.xml";
    static final int PRESENCE= 3302;
    static final String PRESENCE_MODEL = "3302.xml";


//    private static MyLocation locationInstance;

    public static void init(final String[] args, Context ctx) {
        // Set endpoint name
        String endpoint;
        endpoint = "Angela_client";
        LOG.info("ENDPOINT: "+endpoint);

        // Get server URI
        String serverURI;
        //serverURI = "coap://localhost:" + LwM2m.DEFAULT_COAP_PORT;
        serverURI = "coap://leshan.eclipseprojects.io:" + LwM2m.DEFAULT_COAP_PORT;
        LOG.warn(serverURI);


        try {
            List<ObjectModel> modelDefault = ObjectLoader.loadDefault();
            List<ObjectModel> models = new ArrayList<>();
            models.addAll(modelDefault);

            InputStream is = ctx.getResources().getAssets().open(ILLUMINANCE_MODEL);
            List<ObjectModel> my_custom_model = ObjectLoader.loadDdfFile(is, ILLUMINANCE_MODEL);
            models.addAll(my_custom_model);
            LOG.info("Loading model " + ILLUMINANCE_MODEL+" "+models.size());

            is = ctx.getResources().getAssets().open(PRESENCE_MODEL);
            my_custom_model = ObjectLoader.loadDdfFile(is, PRESENCE_MODEL);
            models.addAll(my_custom_model);
            LOG.info("Loading model " + PRESENCE_MODEL+" "+models.size());

            final LwM2mModel model = new StaticModel(models);

            ObjectsInitializer initializer = new ObjectsInitializer(model);
            initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
            initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));

            initializer.setInstancesForObject(DEVICE, new MyDevice());
            initializer.setInstancesForObject(ILLUMINANCE, new LightSensor());
            initializer.setInstancesForObject(PRESENCE, new PresenceSensor());

            LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
            builder.setLocalAddress("0.0.0.0",0);
            builder.setObjects(initializer.createAll());
            final LeshanClient client = builder.build();

            // Start the client
            client.start();
        } catch (Exception e) {
            LOG.error("Unable to create and start client ...");
            e.printStackTrace();
            return;
        }
    }
}
