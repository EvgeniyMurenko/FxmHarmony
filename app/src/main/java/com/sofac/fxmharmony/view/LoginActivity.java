package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

/**
 * Activity login & password authorization, validation input field, if validate data start TasksActivity.class
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static SharedPreferences preferences;
    Intent intent;
    private static long backPressed;
    EditText editPassword, editLogin;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        buttonLogin.setOnClickListener(this);
        intent = new Intent (this, TasksActivity.class);
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

        if("".equals(password)&&"".equals(login)){
            Toast.makeText(LoginActivity.this, getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
        } else {
            CheckAuthorizationOnServer task = new CheckAuthorizationOnServer();
            task.execute(editLogin.getText().toString(),editPassword.getText().toString());

          /*  preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_AUTHORIZATION, true);
            editor.apply();
            editor.commit();*/
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

        ServerResponse<StaffInfo> staffInfoServerResponse;

        @Override
        protected void onPreExecute() {
            //on pre execute
        }

        @Override
        protected String doInBackground(String... urls) {

            SharedPreferences sharedPref =  PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            Authorization authorization = new Authorization(urls[0],urls[1], sharedPref.getString(Constants.GOOGLE_CLOUD_PREFERENCE,""));

            ServerRequest serverRequest = new ServerRequest(Constants.AUTHORIZATION_REQUEST, authorization);
            DataManager dataManager = DataManager.getInstance();
            staffInfoServerResponse = dataManager.sendAuthorizationRequest(serverRequest);


            if (staffInfoServerResponse != null) {
                return staffInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i(result);

            if(result.equals(Constants.REQUEST_SUCCESS)){

                StaffInfo staffInfo = staffInfoServerResponse.getDataTransferObject();

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.STAFF_PROFILE , staffInfo);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, Constants.REQUEST_SUCCESS, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, Constants.SERVER_REQUEST_ERROR, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
