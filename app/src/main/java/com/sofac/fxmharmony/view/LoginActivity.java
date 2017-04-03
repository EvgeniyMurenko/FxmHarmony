package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.Authorization;
import com.sofac.fxmharmony.data.dto.StaffInfo;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.NAME_LOGIN_STAFF;

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
            CheckAuthorizationOnServer task = new CheckAuthorizationOnServer();
            task.execute();

            preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_AUTHORIZATION, true);
            editor.apply();
            editor.commit();

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    private class CheckAuthorizationOnServer extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //on pre execute
        }

        @Override
        protected String doInBackground(String... urls) {

            Authorization authorization = new Authorization("1", "2", "3");
            ServerRequest serverRequest = new ServerRequest(Constants.AUTHORIZATION_REQUEST, authorization);
            DataManager dataManager = DataManager.getInstance();
            ServerResponse<StaffInfo> staffInfoServerResponse = dataManager.sendAuthorizationRequest(serverRequest);


            if (staffInfoServerResponse != null) {
                return staffInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i(result);
            Toast.makeText(LoginActivity.this, Constants.SERVER_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

}
