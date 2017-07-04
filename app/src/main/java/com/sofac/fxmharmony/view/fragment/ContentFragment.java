package com.sofac.fxmharmony.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;
import static com.sofac.fxmharmony.R.id.textView;

public class ContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailTaskActivity;
    SwipeRefreshLayout groupSwipeRefreshLayout;
    public AdapterPushListView adapterTasksListView;
    ListView listViewPush;
    ArrayList<PushMessage> pushMessages;
    TextView textNoMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_push, container, false);
        listViewPush = (ListView) rootView.findViewById(R.id.idListPush);
        textNoMessages = (TextView) rootView.findViewById(R.id.idTextNoMessages);
        listViewPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (pushMessages != null) {
                    intentDetailTaskActivity.putExtra(ONE_PUSH_MESSAGE_DATA, pushMessages.get(position));
                    startActivity(intentDetailTaskActivity);
                }
            }
        });

        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }


    @Override
    public void onResume() {
        updateViewList();
        super.onResume();
    }


    protected void updateViewList() {

        pushMessages = (ArrayList<PushMessage>) PushMessage.listAll(PushMessage.class);

        if (pushMessages != null) {
            textNoMessages.setVisibility(View.INVISIBLE);
            adapterTasksListView = new AdapterPushListView(getActivity(), pushMessages);
            listViewPush.setAdapter(adapterTasksListView);
            adapterTasksListView.notifyDataSetChanged();
        } else {
            textNoMessages.setVisibility(View.VISIBLE)
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getActivity().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (item.getItemId()) {
//            case R.id.menu_update:
//                updateViewList();
//                break;
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

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        groupSwipeRefreshLayout.setRefreshing(false);

    }

}


