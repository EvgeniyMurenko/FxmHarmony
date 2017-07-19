package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.PUSH_MESSAGES_STATE;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class AppMethods {

    public static int getPushState(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(PUSH_MESSAGES_STATE, MODE_PRIVATE);
        return sPref.getInt("State", Constants.PUSH_ON);
    }

    public static void changePushState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PUSH_MESSAGES_STATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int state = sharedPreferences.getInt("State", Constants.PUSH_ON);

        if (state == Constants.PUSH_ON) {
            editor.putInt("State", Constants.PUSH_OFF);
        } else {
            editor.putInt("State", Constants.PUSH_ON);
        }

        editor.apply();
    }

    public static String getAvatarImageUrl(Context context) {
        ManagerInfoDTO managerInfoDTO = ManagerInfoDTO.findById(ManagerInfoDTO.class, getUserId(context));
        String fileName = managerInfoDTO.getAvatarImage();
        return BASE_URL + "get-file/avatar/" + fileName;
    }

    public static void saveAvatarImageName(Context context, String avatarName) {

        ManagerInfoDTO managerInfoDTO = ManagerInfoDTO.findById(ManagerInfoDTO.class, getUserId(context));
        managerInfoDTO.setAvatarImage(avatarName);
        managerInfoDTO.save();

    }

    public static Long getUserId(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        return sharedPreferences.getLong(USER_ID_PREF, 0);

    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public static void saveUserName(Context context, String newName) {
        ManagerInfoDTO managerInfoDTO = ManagerInfoDTO.findById(ManagerInfoDTO.class, getUserId(context));
        managerInfoDTO.setName(newName);

    }

    public static String getUserName(Context context) {
        ManagerInfoDTO managerInfoDTO = ManagerInfoDTO.findById(ManagerInfoDTO.class, getUserId(context));
        return managerInfoDTO.getName();
    }

    public static void putAvatarIntoImageView(Context context, ImageView imageView) {
        Glide.with(context)
                .load(AppMethods.getAvatarImageUrl(context))
                .error(R.drawable.icon_toolbar)
                .override(AVATAR_IMAGE_SIZE , AVATAR_IMAGE_SIZE)
                .centerCrop()
                .into(imageView);
    }

    public static int getPxFromDp(int dimensionDp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

}



  /*  public static String getLanguage(Context context ){
        SharedPreferences sPref = context.getSharedPreferences("Locale", MODE_PRIVATE);
        return sPref.getString("Locale", Locale.ENGLISH.);
    }
*/
