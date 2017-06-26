package com.sofac.fxmharmony.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.GravityCompat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;
import com.sofac.fxmharmony.view.fragment.ContentFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_ALL_POSTS_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;
import static java.lang.Long.getLong;

public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static long backPressed;
    private FragmentPagerAdapter fragmentAdapter;

    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, CreatePost.class),1);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {Timber.e("!!!!!!"); return;}
        SharedPreferences sharedPreferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Long idUser = sharedPreferences.getLong(USER_ID_PREF,0L);
        new GroupExchangeOnServer<PostDTO>(new PostDTO(null,idUser,"FXM Group",null,data.getStringExtra("PostText")), WRITE_POST_REQUEST).execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new ContentFragment(), "Content");
        fragmentAdapter.addFragment(new GroupFragment(), "Group");

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    floatingActionButton.setVisibility(View.VISIBLE);
                } else {
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }

    private class GroupExchangeOnServer<T> extends AsyncTask<String, Void, String> {

        private ServerResponse<List<PostDTO>> loadAllPostsServerResponse;
        private ServerResponse<List<CommentDTO>> loadCommentsServerResponse;
        private ServerResponse serverResponse;

        private String type;
        private T serverObject;

        ProgressDialog pd = new ProgressDialog(MainActivity.this, R.style.MyTheme);

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
                case LOAD_ALL_POSTS_REQUEST:
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

                case Constants.DELETE_COMMENT_REQUEST:

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
                Toast.makeText(MainActivity.this, "Create post error!", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }
}