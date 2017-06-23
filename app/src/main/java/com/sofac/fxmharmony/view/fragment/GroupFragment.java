package com.sofac.fxmharmony.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sofac.fxmharmony.view.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;


public class GroupFragment extends ListFragment {


    public AdapterPostGroup adapterPostGroup;
    ListView listViewPost;
    ArrayList<PostDTO> postDTOs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewPost = this.getListView();
        updateViewList();

    }

    @Override
    public void onResume() {
        updateViewList();
        super.onResume();
    }

    protected void updateViewList() {
        postDTOs = new ArrayList<>();
        postDTOs.add(new PostDTO(1L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(2L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(3L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(4L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(5L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(6L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(7L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(8L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(9L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(10L,2L,new Date(),"ASDSAdsds sds sdas asds"));
        postDTOs.add(new PostDTO(11L,2L,new Date(),"ASDSAdsds sds sdas asds"));

            adapterPostGroup = new AdapterPostGroup(getActivity(), postDTOs);

            GroupFragment.this.setListAdapter(adapterPostGroup);

            //listViewPost = GroupFragment.this.getListView();

    }


   /* public void onClick(View v) {
        String password ="";
        String login = "";

        new GroupExchangeOnServer().execute();

    }*/

    private class GroupExchangeOnServer<T> extends AsyncTask<String, Void, String> {

        private ServerResponse<List<PostDTO>> loadAllPostsServerResponse;
        private ServerResponse<List<CommentDTO>> loadCommentsServerResponse;
        private ServerResponse serverResponse;

        private String type;
        private T serverObject;

        private GroupExchangeOnServer(T serverObject, String type) {
            this.serverObject = serverObject;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            // progress dialog
        }

        @Override
        @SuppressWarnings("unchecked")
        protected String doInBackground(String... urls) {

            ServerRequest serverRequest = new ServerRequest(type, null);
            DataManager dataManager = DataManager.getInstance();

            switch (type) {
                case Constants.LOAD_ALL_POSTS_REQUEST:

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                case Constants.LOAD_COMMENTS_REQUEST: {

                    Long postID = (Long) serverObject;
                    serverRequest.setDataTransferObject(postID);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
                case Constants.WRITE_POST_REQUEST: {

                    PostDTO postDTO = (PostDTO) serverObject;
                    serverRequest.setDataTransferObject(postDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
                case Constants.WRITE_COMMENT_REQUEST: {

                    CommentDTO commentDTO = (CommentDTO) serverObject;
                    serverRequest.setDataTransferObject(commentDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
                case Constants.DELETE_POST_REQUEST: {

                    Long postID = (Long) serverObject;
                    serverRequest.setDataTransferObject(postID);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
                case DELETE_COMMENT_REQUEST:

                    Long commentID = (Long) serverObject;
                    serverRequest.setDataTransferObject(commentID);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                case UPDATE_POST_REQUEST: {

                    PostDTO postDTO = (PostDTO) serverObject;
                    serverRequest.setDataTransferObject(postDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
                case UPDATE_COMMENT_REQUEST: {

                    CommentDTO commentDTO = (CommentDTO) serverObject;
                    serverRequest.setDataTransferObject(commentDTO);

                    serverResponse = dataManager.postGroupRequest(serverRequest, type);

                    break;
                }
            }

            if (serverResponse != null) {
                return serverResponse.getResponseStatus();
            }


            return Constants.SERVER_REQUEST_ERROR;
        }


        @Override
        protected void onPostExecute(String result) {
            Timber.e("Response Server: " + result);

            if (result.equals(Constants.REQUEST_SUCCESS)) {

                switch (type) {
                    case Constants.LOAD_ALL_POSTS_REQUEST:

                        loadAllPostsServerResponse = serverResponse;

                        break;
                    case Constants.LOAD_COMMENTS_REQUEST: {

                        loadCommentsServerResponse = serverResponse;

                        break;
                    }
                }
            } else {
                Toast.makeText(getActivity(), R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }
        }
    }
}



