package com.sofac.fxmharmony.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.service.BackgroundFileUploadService;

import java.util.ArrayList;

public class RequestMethods {


    public static void startServiceAttachLoadFilesToPost(Context context, ArrayList<Uri> uris , Long postID){
        Intent serviceIntent = new Intent(context, BackgroundFileUploadService.class);
        serviceIntent.putExtra("type" , Constants.ATTACH_LOAD_FXM_POST_FILES);
        serviceIntent.putExtra("uri", uris);
        serviceIntent.putExtra("serverObject" , postID);
        context.startService(serviceIntent);
    }
}
