package com.sofac.fxmharmony.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterTasksListView;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.MessageTask;
import com.sofac.fxmharmony.data.dto.StaffInfo;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.TASK_INFO;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class CasesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static long backPressed;
    public ArrayList<MessageTask> listStaff;
    public AdapterTasksListView adapterTasksListView;
    private static StaffInfo staffInfo;
    private Intent intentMainActivity;
    private Intent intentTasksActivity;
    private Intent intentDetailTaskActivity;
    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        intentMainActivity = new Intent(this, MainActivity.class);
        intentTasksActivity = new Intent(this, TasksActivity.class);
        intentDetailTaskActivity = new Intent(this, DetailTaskActivity.class);
    }

    @Override
    protected void onResume() {
        SharedPreferences preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        GetStaffInfoOnServer task = new GetStaffInfoOnServer();
        task.execute(preferences.getLong(USER_ID_PREF, 0));
        super.onResume();
    }

    public void viewListTasks() {
        ListView listViewTasks = (ListView) findViewById(R.id.listViewTasks);

        adapterTasksListView = new AdapterTasksListView(this, listStaff);
        listViewTasks.setAdapter(adapterTasksListView);

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                intentDetailTaskActivity.putExtra(TASK_INFO, listStaff.get(position));
                startActivity(intentDetailTaskActivity);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_cases) {

        } else if (id == R.id.nav_tasks) {
            intentTasksActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentTasksActivity);
        } else if (id == R.id.nav_exit) {
            SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(IS_AUTHORIZATION, false);
            editor.apply();
            editor.commit();

            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMainActivity);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class GetStaffInfoOnServer extends AsyncTask<Long, Void, String> {

        ServerResponse<StaffInfo> staffInfoServerResponse;

        @Override
        protected void onPreExecute() {
            //on pre execute
        }

        @Override
        protected String doInBackground(Long... urls) {
            ServerRequest serverRequest = new ServerRequest(Constants.GET_STAFF_INFO_REQUEST, urls[0]);
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

            if (result.equals(Constants.REQUEST_SUCCESS)) {
                staffInfo = staffInfoServerResponse.getDataTransferObject();
                listStaff = (ArrayList<MessageTask>) staffInfo.getMessageTasks();
                Collections.sort(listStaff, new Comparator<MessageTask>() {
                    public int compare(MessageTask messageTask1, MessageTask messageTask2) {
                        return messageTask2.getDate().compareTo(messageTask1.getDate());
                    }
                });
                viewListTasks();
            } else {
                Toast.makeText(CasesActivity.this, R.string.errorSingIn, Toast.LENGTH_SHORT).show();

                preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_AUTHORIZATION, false);
                editor.apply();
                editor.commit();

                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMainActivity);
            }

        }
    }
}