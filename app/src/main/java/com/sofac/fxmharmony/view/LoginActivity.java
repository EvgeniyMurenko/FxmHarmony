package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import static com.sofac.fxmharmony.Constants.LOGIN_STAFF;
import static com.sofac.fxmharmony.Constants.PREFERENCE_AUTHORIZATION;

/**
 * Activity login & password authorization, validation input field, if validate data start TasksActivity.class
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static SharedPreferences preferences;

    private static long backPressed;
    EditText editPassword, editLogin;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        buttonLogin.setOnClickListener(this);
    }

    private void initUI(){
        editPassword = (EditText) findViewById(R.id.editPassword);
        editLogin = (EditText) findViewById(R.id.editLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    @Override
    public void onClick(View v) {
        String password = editPassword.getText().toString();
        String login = editLogin.getText().toString();
        Intent intent = new Intent (this, TasksActivity.class);
        if("".equals(password)&&"".equals(login)){
            Toast.makeText(LoginActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
        } else {
            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREFERENCE_AUTHORIZATION, true);
            editor.putString(LOGIN_STAFF, editLogin.getText().toString());
            editor.commit();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

}
