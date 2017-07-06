package com.sofac.fxmharmony.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sofac.fxmharmony.Constants;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class AppMethods {

    public static int getPushState(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("PushState", MODE_PRIVATE);
        return sPref.getInt("State", Constants.PUSH_ON);
    }

    public static void changePushState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("PushState", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int state = sharedPreferences.getInt("State", Constants.PUSH_ON);

        if (state == Constants.PUSH_ON) {
            editor.putInt("State", Constants.PUSH_OFF);
        } else {
            editor.putInt("State", Constants.PUSH_ON);
        }

        editor.apply();
    }

  /*  public static String getLanguage(Context context ){
        SharedPreferences sPref = context.getSharedPreferences("Locale", MODE_PRIVATE);
        return sPref.getString("Locale", Locale.ENGLISH.);
    }
*/
}
