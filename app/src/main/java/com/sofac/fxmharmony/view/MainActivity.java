package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.Authorization;
import com.sofac.fxmharmony.data.dto.StaffInfo;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends BaseActivity {
    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.i("onCreate()");


        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute();


        /*checkAuthorization();*/
    }

    @Override
    protected void onResume() {
        checkAuthorization();
        super.onResume();
    }

    public void checkAuthorization() {
        preferences = getPreferences(MODE_PRIVATE);
        if (preferences.getBoolean(Constants.PREFERENCE_AUTHORIZATION, false)) {
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


    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

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
        }
    }


}



