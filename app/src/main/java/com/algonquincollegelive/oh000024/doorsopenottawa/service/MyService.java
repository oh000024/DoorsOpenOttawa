package com.algonquincollegelive.oh000024.doorsopenottawa.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpHelper;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Class MyService.
 * <p>
 * Fetch the data at URI.
 * Return an array of Building[] as a broadcast message.
 *
 * @author jake Oh (oh000024@algonquinlive.com)
 */
public class MyService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_RESPONSE = "myServiceResponse";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(REQUEST_PACKAGE);

        String response;
        try {
            response = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        Gson gson = new Gson();
        switch (requestPackage.getMethod()) {
            case GET:
                BuildingPOJO[] buildingArray = gson.fromJson(response, BuildingPOJO[].class);
                messageIntent.putExtra(MY_SERVICE_PAYLOAD, buildingArray);
                break;

            case POST:
            case PUT:
            case DELETE:
                BuildingPOJO building = gson.fromJson(response, BuildingPOJO.class);
                String message;

                message = requestPackage.getMethod() + ": " + building.getNameEN() + "/" + building.getBuildingId();

                messageIntent.putExtra(MY_SERVICE_RESPONSE, message);
                break;
        }
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }
}
