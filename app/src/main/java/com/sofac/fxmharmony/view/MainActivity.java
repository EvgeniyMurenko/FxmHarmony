package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.data.dto.PushMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;

public class MainActivity extends BaseActivity {
    private static long backPressed;
    public Intent intentSplashActivity;

    public AdapterPushListView adapterTasksListView;
    String gsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        intentSplashActivity = new Intent(this, SplashActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onResume() {
        UpdateViewList();
        super.onResume();
    }

    protected void UpdateViewList() {
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        gsonString = preferences.getString(PUSH_MASSEGES, "null");
        if (!"null".equals(gsonString)) {
            Type type = new TypeToken<List<PushMessage>>() {
            }.getType();
            ArrayList<PushMessage> pushMessages = new Gson().fromJson(gsonString, type);

            ListView listViewPush = (ListView) findViewById(R.id.listViewMain);
            adapterTasksListView = new AdapterPushListView(this, pushMessages);
            listViewPush.setAdapter(adapterTasksListView);
        }
//        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
//                intentDetailTaskActivity.putExtra(TASK_INFO, listStaff.get(position));
//                startActivity(intentDetailTaskActivity);
//            }
//        });


//        TextView textView = (TextView) findViewById(R.id.textViewTest);
//        textView.setText(pushMessages.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.menu_exit:
                editor.putBoolean(IS_AUTHORIZATION, false);
                editor.apply();
                intentSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSplashActivity);
                break;
            case R.id.menu_update:
                UpdateViewList();
            case R.id.menu_delete_all:
                editor.putString(PUSH_MASSEGES, "null");
                editor.apply();
                UpdateViewList();
                break;
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
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


}