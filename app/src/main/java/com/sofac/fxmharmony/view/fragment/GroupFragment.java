package com.sofac.fxmharmony.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.view.CreatePost;
import com.sofac.fxmharmony.view.DetailPostActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.USER_SERVICE;
import static com.sofac.fxmharmony.Constants.DELETE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Intent intentDetailPostActivity;
    public AdapterPostGroup adapterPostGroup;
    public ListView listViewPost;
    public ArrayList<PostDTO> postDTOs;
    public SwipeRefreshLayout groupSwipeRefreshLayout;
    SharedPreferences preferences;
    public FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getActivity(), DetailPostActivity.class);
        preferences = getActivity().getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        listViewPost = (ListView) rootView.findViewById(R.id.idListGroup);
        listViewPost.setDivider(null);
        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);

        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(GroupFragment.this.getActivity(), CreatePost.class), 1);
            }
        });

        listViewPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final PostDTO postDTO = postDTOs.get(position);
                if (postDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //builder.setTitle("Pick a color");
                    builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(getActivity(), "Double click 1111!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(getActivity(), "Double click 2222!", Toast.LENGTH_SHORT).show();
                                    new GroupExchangeOnServer<Long>(postDTO.getId(), true, DELETE_POST_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponse() {
                                        @Override
                                        public void processFinish(Boolean isSuccess) {
                                            if (isSuccess) {
                                            }
                                        }
                                    }).execute();
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });

        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(ONE_POST_DATA, postDTOs.get(position));
                    startActivity(intentDetailPostActivity);
                }
            }
        });


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

                    if (postDTOs != null) {
                        adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
                        listViewPost.setAdapter(adapterPostGroup);
                        adapterPostGroup.notifyDataSetChanged();
                    }
                }
            }
        }).execute();


    }

    @Override
    public void onRefresh() {
        groupSwipeRefreshLayout.setRefreshing(true);
        updateViewList(false);
        groupSwipeRefreshLayout.setRefreshing(false);
    }
}



