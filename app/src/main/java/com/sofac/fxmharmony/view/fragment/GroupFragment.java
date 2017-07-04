package com.sofac.fxmharmony.view.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.view.DetailPostActivity;
import java.util.ArrayList;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;

public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailPostActivity;
    public AdapterPostGroup adapterPostGroup;
    ListView listViewPost;
    ArrayList<PostDTO> postDTOs;
    SwipeRefreshLayout groupSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getActivity(), DetailPostActivity.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        listViewPost = (ListView) rootView.findViewById(R.id.idListGroup);
        listViewPost.setDivider(null);
        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(ONE_POST_DATA, postDTOs.get(position));
                    startActivity(intentDetailPostActivity);
                }
            }
        });
        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        updateViewList(true);
        super.onResume();
    }

    protected void updateViewList(Boolean toDoProgressDialog) {

        new GroupExchangeOnServer<PostDTO>(null, toDoProgressDialog, LOAD_ALL_POSTS_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean isSuccess) {
                if (isSuccess) {
                    postDTOs = (ArrayList<PostDTO>) PostDTO.listAll(PostDTO.class);
                }
            }
        }).execute();

        if (postDTOs != null) {
            adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
            listViewPost.setAdapter(adapterPostGroup);
            adapterPostGroup.notifyDataSetChanged();
        }


    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        new GroupExchangeOnServer<PostDTO>(null, false, LOAD_ALL_POSTS_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean isSuccess) {
                groupSwipeRefreshLayout.setRefreshing(false);
            }
        }).execute();

    }
}



