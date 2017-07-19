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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PermissionDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.ChangePost;
import com.sofac.fxmharmony.view.CreatePost;
import com.sofac.fxmharmony.view.DetailPostActivity;
import java.util.ArrayList;

import timber.log.Timber;

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
    public static Long idPost;
    public static PostDTO postDTO;
    public Intent intentChangePost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getActivity(), DetailPostActivity.class);
        intentChangePost = new Intent(this.getActivity(), ChangePost.class);
        preferences = getActivity().getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container, false);
        listViewPost = (ListView) rootView.findViewById(R.id.idListGroup);
        listViewPost.setDivider(null);
        groupSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        groupSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);

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
                postDTO = postDTOs.get(position);
                GroupFragment.idPost = postDTOs.get(position).getServerID();
                PermissionDTO permissionDTO = PermissionDTO.findById(PermissionDTO.class, preferences.getLong(USER_ID_PREF, 1L));
                Timber.e("!!!!!!!!!!"+permissionDTO.toString());


                if (postDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L) || permissionDTO.getSuperAdminPermission()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: //Edit
                                    writePost();
                                    break;
                                case 1: //Delete
                                    new GroupExchangeOnServer<>(GroupFragment.idPost, true, DELETE_POST_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponse() {
                                        @Override
                                        public void processFinish(Boolean isSuccess) {
                                            if (isSuccess) {
                                                updateViewList(true);
                                                Toast.makeText(getActivity(), "Post was delete!", Toast.LENGTH_SHORT).show();
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

    public void writePost(){
        intentChangePost.putExtra(ONE_POST_DATA, postDTO);
        startActivity(intentChangePost);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_group, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update_list_post:
                updateViewList(true);
                break;
            case R.id.menu_write_post:
                startActivityForResult(new Intent(GroupFragment.this.getActivity(), CreatePost.class), 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}



