package com.sofac.fxmharmony.view.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.ListFragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.R.attr.type;
import static android.content.Context.MODE_PRIVATE;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class ContentFragment extends ListFragment {


    private static long backPressed;
    public Intent intentSplashActivity;
    public Intent intentDetailTaskActivity;

    public AdapterPushListView adapterTasksListView;
    String gsonString;
    ListView listViewPush;
    ArrayList<PushMessage> pushMessages;

    ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listViewPush = this.getListView();
        updateViewList();
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);
        Timber.i("listViewState +" + listViewPush);
    }


    @Override
    public void onResume() {
        updateViewList();
        super.onResume();
    }



    protected void updateViewList() {
        pushMessages = (ArrayList<PushMessage>) PushMessage.listAll(PushMessage.class);

        if (pushMessages != null) {

            adapterTasksListView = new AdapterPushListView(getActivity(), pushMessages);

            ContentFragment.this.setListAdapter(adapterTasksListView);

            listViewPush = ContentFragment.this.getListView();

            listViewPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                    if (pushMessages != null) {
                        intentDetailTaskActivity.putExtra(ONE_PUSH_MESSAGE_DATA, pushMessages.get(position));
                        startActivity(intentDetailTaskActivity);
                    }
                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.menu_exit:
                editor.putBoolean(IS_AUTHORIZATION, false);
                editor.apply();
                intentSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSplashActivity);
                break;
            case R.id.menu_update:
                updateViewList();
                break;
            case R.id.menu_delete_all:
                editor.putString(PUSH_MASSEGES, "");
                editor.apply();
                if (pushMessages != null) {
                    pushMessages.clear();
                    adapterTasksListView.notifyDataSetChanged();
                }
                break;
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
    }

}


