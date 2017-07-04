package com.sofac.fxmharmony.view.fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPushListView;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;

public class ContentFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailTaskActivity;
    SwipeRefreshLayout groupSwipeRefreshLayout;
    public AdapterPushListView adapterTasksListView;
    ListView listViewPush;
    ArrayList<PushMessage> pushMessages;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        intentDetailTaskActivity = new Intent(this.getActivity(), DetailPushMessageActivity.class);

        listViewPush = this.getListView();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_push, null);
        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.idRefresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        updateViewList();
    }


    protected void updateViewList() {

        if((PushMessage.count(PushMessage.class, null, null))>0) pushMessages = (ArrayList<PushMessage>) PushMessage.listAll(PushMessage.class);

        if (pushMessages != null) {
            adapterTasksListView = new AdapterPushListView(getActivity(), pushMessages);
            ContentFragment.this.getListView().setAdapter(adapterTasksListView);
            adapterTasksListView.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all:
                if (pushMessages != null) {
                    pushMessages.clear();
                    PushMessage.deleteAll(PushMessage.class);
                    adapterTasksListView.notifyDataSetChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        updateViewList();
        groupSwipeRefreshLayout.setRefreshing(false);
    }

}


