package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.i("onCreate()");

        checkAuthorization();
    }

    @Override
    protected void onResume() {
        checkAuthorization();
        super.onResume();
    }

    public void checkAuthorization(){
        preferences = getPreferences(MODE_PRIVATE);
        if(preferences.getBoolean(Constants.PREFERENCE_AUTHORIZATION, false)){
            startTasksActivity();
        } else {
            startLoginActivity();
        }
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void startTasksActivity() {
        Intent intent = new Intent(this, TasksActivity.class);
        startActivity(intent);
    }
}
