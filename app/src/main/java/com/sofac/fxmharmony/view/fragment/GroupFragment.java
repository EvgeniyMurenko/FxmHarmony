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

        String postText =  getActivity().getIntent().getStringExtra("postText");
        if (!"".equals(postText) && postText != null){

            new GroupExchangeOnServer<PostDTO>(new PostDTO(1l, 3l,"Name",null,postText), WRITE_POST_REQUEST).execute();
            getActivity().getIntent().putExtra("postText", "");
        }

    }



    @Override
    public void onResume() {
        updateViewList();
        super.onResume();
    }

    protected void updateViewList() {
        new GroupExchangeOnServer<PostDTO>(null, LOAD_ALL_POSTS_REQUEST).execute();

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


    private class GroupExchangeOnServer<T> extends AsyncTask<String, Void, String> {

        private ServerResponse<List<PostDTO>> loadAllPostsServerResponse;
        private ServerResponse<List<CommentDTO>> loadCommentsServerResponse;
        private ServerResponse serverResponse;

        private String type;
        private T serverObject;

       ProgressDialog pd = new ProgressDialog(GroupFragment.this.getActivity(), R.style.MyTheme);

        private GroupExchangeOnServer(T serverObject, String type) {
            this.serverObject = serverObject;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            Timber.i("on pre");
            pd.setCancelable(false);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pd.show();
        }


        @Override
        @SuppressWarnings("unchecked")
        protected String doInBackground(String... urls) {

            ServerRequest serverRequest = new ServerRequest(type, null);
            DataManager dataManager = DataManager.getInstance();

            PostDTO postDTO;
            CommentDTO commentDTO;

            switch (type) {
                case Constants.LOAD_ALL_POSTS_REQUEST:
                    Timber.i(serverRequest.toString());
                    serverResponse = dataManager.postGroupRequest(serverRequest, type);//postID = serverRequest

                    break;

                case Constants.LOAD_COMMENTS_REQUEST:

                    serverRequest.setDataTransferObject(serverObject);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

                case WRITE_POST_REQUEST:

                    postDTO = (PostDTO) serverObject;

                    serverRequest = new ServerRequest<PostDTO>(type, postDTO);
                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

                case Constants.WRITE_COMMENT_REQUEST:

                    commentDTO = (CommentDTO) serverObject;
                    serverRequest.setDataTransferObject(commentDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

                case Constants.DELETE_POST_REQUEST:

                    serverRequest.setDataTransferObject(serverObject); //postID = serverRequest

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

                case DELETE_COMMENT_REQUEST:

                    Long commentID = (Long) serverObject;
                    serverRequest.setDataTransferObject(commentID);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                case UPDATE_POST_REQUEST:

                    postDTO = (PostDTO) serverObject;
                    serverRequest.setDataTransferObject(postDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

                case UPDATE_COMMENT_REQUEST:

                    commentDTO = (CommentDTO) serverObject;
                    serverRequest.setDataTransferObject(commentDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;

            }

            if (serverResponse != null) {
                return serverResponse.getResponseStatus().toString();
            }


            return Constants.SERVER_REQUEST_ERROR.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            Timber.e("Response Server: " + result);

            if (result.equals(Constants.REQUEST_SUCCESS)) {

                switch (type) {
                    case LOAD_ALL_POSTS_REQUEST:

                        loadAllPostsServerResponse = serverResponse;
                        ArrayList<PostDTO> postDTOs = (ArrayList<PostDTO>) loadAllPostsServerResponse.getDataTransferObject();
                        PostDTO.deleteAll(PostDTO.class);
                        for (int i = 0; i < postDTOs.size(); i++) {
                            postDTOs.get(i).save();
                        }

                        break;
                    case Constants.LOAD_COMMENTS_REQUEST: {

                        loadCommentsServerResponse = serverResponse;
                        break;
                    }
                }
            } else {
                Toast.makeText(getActivity(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }
}



