package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.sofac.fxmharmony.R;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;


public class SplashActivity extends BaseActivity {
    public static SharedPreferences preferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        checkAuthorization();
        super.onResume();
    }

    public void checkAuthorization() {
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        if (preferences.getBoolean(IS_AUTHORIZATION, false)) {
            startNavigationActivity();
        } else {
            startLoginActivity();
        }
    }

    public void startLoginActivity() {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startNavigationActivity() {
        intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}



