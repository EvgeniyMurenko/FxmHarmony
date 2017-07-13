package com.sofac.fxmharmony.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.Authorization;
import com.sofac.fxmharmony.data.dto.ManagerInfoDTO;
import com.sofac.fxmharmony.data.dto.PermissionDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

/**
 * Activity login & password authorization, validation input field, if validate data start MainActivity.class
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
        intent = new Intent(this, NavigationActivity.class);
    }

    private void initUI() {
        editPassword = (EditText) findViewById(R.id.editPassword);
        editLogin = (EditText) findViewById(R.id.editLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
    }

    @Override
    public void onClick(View v) {
        String password = editPassword.getText().toString();
        String login = editLogin.getText().toString();

        if ("".equals(password) && "".equals(login)) {
            Toast.makeText(LoginActivity.this, getString(R.string.fieldEmpty), Toast.LENGTH_SHORT).show();
        } else {
            CheckAuthorizationOnServer task = new CheckAuthorizationOnServer();
            task.execute(editLogin.getText().toString(), editPassword.getText().toString());
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
        ServerResponse<ManagerInfoDTO> managerInfoServerResponse;
        ProgressDialog pd = new ProgressDialog(LoginActivity.this, R.style.MyTheme);

        @Override
        protected void onPreExecute() {
            pd.setCancelable(false);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            Authorization authorization = new Authorization(urls[0], urls[1], sharedPref.getString(Constants.GOOGLE_CLOUD_PREFERENCE, ""));

            ServerRequest serverRequest = new ServerRequest(Constants.AUTHORIZATION_REQUEST, authorization);
            DataManager dataManager = DataManager.getInstance();
            managerInfoServerResponse = dataManager.sendAuthorizationRequest(serverRequest);

            if (managerInfoServerResponse != null) {
                return managerInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.e("Response Server: " + result);

            if (Constants.REQUEST_SUCCESS.equals(result)) {
                ManagerInfoDTO.deleteAll(ManagerInfoDTO.class);
                PermissionDTO.deleteAll(PermissionDTO.class);

                ManagerInfoDTO managerInfoDTO = managerInfoServerResponse.getDataTransferObject();
                managerInfoDTO.save();

                PermissionDTO permissionDTO = managerInfoServerResponse.getDataTransferObject().getPermissions();
                permissionDTO.setId(1L);
                permissionDTO.save();

                Toast.makeText(LoginActivity.this, permissionDTO.getTranslatePermission().toString(), Toast.LENGTH_SHORT).show();

                preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_AUTHORIZATION, true);
                editor.apply();
                editor.commit();

                SharedPreferences preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
                SharedPreferences.Editor editorUser = preferences.edit();
                editorUser.putLong(USER_ID_PREF, managerInfoDTO.getIdServer());
                editorUser.apply();
                editorUser.commit();

                Timber.e(preferences.getLong(USER_ID_PREF, 0L) + "");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }

}
