package com.algonquincollegelive.oh000024.doorsopenottawa.service;


import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import com.algonquincollegelive.oh000024.doorsopenottawa.R;
import com.algonquincollegelive.oh000024.doorsopenottawa.model.BuildingPOJO;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.HttpHelper;
import com.algonquincollegelive.oh000024.doorsopenottawa.utils.RequestPackage;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;


// TODO #3 - create a new intent service: UploadImageFileService
/**
 * UploadImageFileService.
 *
 *  @author jake Oh (oh000024@algonquinlive.com)
 */
public class UploadImageFileService extends IntentService {

    public static final String UPLOAD_IMAGE_FILE_SEVICE_MESSAGE = "UploadImageFileServiceMessage";
    public static final String UPLOAD_IMAGE_FILE_SERVICE_RESPONSE = "UploadImageFileServiceResponse";
    public static final String UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION = "UploadImageFileServiceException";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public UploadImageFileService() {
        super("UploadImageFileService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(REQUEST_PACKAGE);

        // hard-coded image from res/drawable
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.i_am_smiling);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);

        String response;
        try {
            // TODO #4 - replace with your username and password
            response = HttpHelper.uploadFile(requestPackage, "oh000024", "password", "jakepic.jpg", baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(UPLOAD_IMAGE_FILE_SEVICE_MESSAGE);
            messageIntent.putExtra(UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Intent messageIntent = new Intent(UPLOAD_IMAGE_FILE_SEVICE_MESSAGE);
        Gson gson = new Gson();
        try {
//            String temp = response.split("}")[0];
            BuildingPOJO building = gson.fromJson(response, BuildingPOJO.class);
//            BuildingPOJO building = gson.fromJson(response, BuildingPOJO.class);
            messageIntent.putExtra(UPLOAD_IMAGE_FILE_SERVICE_RESPONSE,
                    requestPackage.getMethod() + ": " + building.getImage());

            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
