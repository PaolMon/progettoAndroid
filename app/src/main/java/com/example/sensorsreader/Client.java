package com.example.sensorsreader;

import android.content.Context;
import android.os.Build;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.client.object.Device;
import org.eclipse.leshan.core.LwM2m;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.model.StaticModel;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.core.util.Hex;
import org.eclipse.leshan.core.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.ContentHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.eclipse.leshan.core.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

public class Client {

    private static final Logger LOG = LoggerFactory.getLogger(Client.class);

//    private static MyLocation locationInstance;

    public static void init(final String[] args, Context ctx) {
        // Get endpoint name
        String endpoint;
        endpoint = "Paolo."+Build.DEVICE + "-" + Build.ID;
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

            final LwM2mModel model = new StaticModel(models);

            ObjectsInitializer initializer = new ObjectsInitializer(model);
            initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));
            initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));

            initializer.setInstancesForObject(DEVICE, new Device("Eclipse Leshan", "model12345", "12345", "U"));

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
