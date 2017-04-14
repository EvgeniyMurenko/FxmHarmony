package com.sofac.fxmharmony.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.sofac.fxmharmony.adapter.AdapterCasesListView;
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
import static com.sofac.fxmharmony.Constants.VIEW_CASES_LIST;
import static com.sofac.fxmharmony.Constants.VIEW_TASKS_LIST;
import static com.sofac.fxmharmony.Constants.WHAT_KIND_VIEW_LIST;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static long backPressed;
    public ArrayList<MessageTask> listStaff;
    public AdapterTasksListView adapterTasksListView;
    public AdapterCasesListView adapterCasesListView;
    private static StaffInfo staffInfo;
    private Intent intentMainActivity;
    private Intent intentDetailCaseActivity;
    private Intent intentDetailTaskActivity;
    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        intentMainActivity = new Intent(this, SplashActivity.class);
        intentDetailCaseActivity = new Intent(this, DetailCaseActivity.class);
        intentDetailTaskActivity = new Intent(this, DetailTaskActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        uploadListFromServer();
        super.onResume();
    }

    public void uploadListFromServer() {
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        GetListFromServer task = new GetListFromServer();
        Long lg = preferences.getLong(USER_ID_PREF, 0);
        Timber.e(lg.toString());
        task.execute(preferences.getLong(USER_ID_PREF, 0), preferences.getLong(WHAT_KIND_VIEW_LIST, VIEW_TASKS_LIST));
    }

    public void viewListCases() {
        ListView listViewCases = (ListView) findViewById(R.id.listViewMain);

        adapterCasesListView = new AdapterCasesListView(this, listStaff);
        listViewCases.setAdapter(adapterCasesListView);

        listViewCases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                intentDetailCaseActivity.putExtra(TASK_INFO, listStaff.get(position));
                startActivity(intentDetailCaseActivity);
            }
        });
    }

    public void viewListTasks() {
        ListView listViewTasks = (ListView) findViewById(R.id.listViewMain);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences.Editor editor = preferences.edit();
        if (id == R.id.nav_cases) {
            setTitle(getResources().getString(R.string.cases_name));
            editor.putLong(WHAT_KIND_VIEW_LIST, VIEW_CASES_LIST);
            uploadListFromServer();
        } else if (id == R.id.nav_tasks) {
            setTitle(getResources().getString(R.string.tasks_name));
            editor.putLong(WHAT_KIND_VIEW_LIST, VIEW_TASKS_LIST);
            uploadListFromServer();
        } else if (id == R.id.nav_exit) {
            editor.putBoolean(IS_AUTHORIZATION, false);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMainActivity);
        }
        editor.apply();
        editor.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Request to server class
     */
    private class GetListFromServer extends AsyncTask<Long, Void, String> {

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

            if (urls[1].equals(Constants.VIEW_CASES_LIST)) {

            } else if (urls[1].equals(Constants.VIEW_TASKS_LIST)) {

            }

            if (staffInfoServerResponse != null) {
                return staffInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i(result);
            if (result.equals(Constants.REQUEST_SUCCESS)) {

//                if (urls[1].equals(Constants.VIEW_CASES_LIST)) {
//                    ServerRequest serverRequest = new ServerRequest(GET_STAFF_INFO_REQUEST, "");
//                    DataManager dataManager = DataManager.getInstance();
//                    serverResponse = dataManager.sendAuthorizationRequest(serverRequest);
//                } else if (urls[1].equals(Constants.VIEW_CASES_LIST)) {
                staffInfo = staffInfoServerResponse.getDataTransferObject();
                listStaff = (ArrayList<MessageTask>) staffInfo.getMessageTasks();
                Collections.sort(listStaff, new Comparator<MessageTask>() {
                    public int compare(MessageTask messageTask1, MessageTask messageTask2) {
                        return messageTask2.getDate().compareTo(messageTask1.getDate());
                    }
                });
                viewListTasks();
//                }

            } else {
                Toast.makeText(MainActivity.this, R.string.errorSingIn, Toast.LENGTH_SHORT).show();

//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putBoolean(IS_AUTHORIZATION, false);
//                editor.apply();
//                editor.commit();
//
//                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentMainActivity);
            }

        }
    }
}