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
import android.widget.ListView;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import com.sofac.fxmharmony.view.SplashActivity;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;

public class ContentFragment extends ListFragment {

    public Intent intentSplashActivity;
    public Intent intentDetailTaskActivity;

    public AdapterPushListView adapterTasksListView;
    ListView listViewPush;
    ArrayList<PushMessage> pushMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        listViewPush = this.getListView();
        intentSplashActivity = new Intent(this.getActivity(), SplashActivity.class);
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);
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

            adapterTasksListView.notifyDataSetChanged();

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
                    PushMessage.deleteAll(PushMessage.class);
                    adapterTasksListView.notifyDataSetChanged();
                }
                break;
        }
        editor.commit();
        return super.onOptionsItemSelected(item);
    }



}


