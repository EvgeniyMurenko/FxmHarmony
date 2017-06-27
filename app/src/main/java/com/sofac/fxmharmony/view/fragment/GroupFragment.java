package com.sofac.fxmharmony.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;
import com.sofac.fxmharmony.view.LoginActivity;
import com.sofac.fxmharmony.view.MainActivity;

import com.sofac.fxmharmony.view.DetailPostActivity;
import com.sofac.fxmharmony.view.DetailPushMessageActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;

public class GroupFragment extends ListFragment {


    public Intent intentDetailPostActivity;
    public AdapterPostGroup adapterPostGroup;
    ListView listViewPost;
    ArrayList<PostDTO> postDTOs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentDetailPostActivity = new Intent(this.getActivity(), DetailPostActivity.class);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewPost = this.getListView();
        getListView().setDivider(null);

//        String postText =  getActivity().getIntent().getStringExtra("postText");
//        if (!"".equals(postText) && postText != null){
//
//            new GroupExchangeOnServer<PostDTO>(new PostDTO(1l, 3l,"Name",null,postText), WRITE_POST_REQUEST).execute();
//            getActivity().getIntent().putExtra("postText", "");
//        }
    }



    @Override
    public void onResume() {
        updateViewList();
        super.onResume();


        new GroupExchangeOnServer<PostDTO>(null, LOAD_ALL_POSTS_REQUEST, getActivity(), new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean isSuccess) {
                if (isSuccess) updateViewList();
            }
        }).execute();
    }

    protected void updateViewList() {


        postDTOs = (ArrayList<PostDTO>) PostDTO.listAll(PostDTO.class);

        if (postDTOs != null) {
            adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);
            GroupFragment.this.setListAdapter(adapterPostGroup);
            adapterPostGroup.notifyDataSetChanged();
        }

        listViewPost = GroupFragment.this.getListView();

        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(ONE_POST_DATA, postDTOs.get(position));
                    startActivity(intentDetailPostActivity);
                }
            }
        });



    }


}



