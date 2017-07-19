package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.util.FxmPostFile;
import com.sofac.fxmharmony.util.PermissionManager;
import com.sofac.fxmharmony.util.RequestMethods;
import com.sofac.fxmharmony.view.customView.FileItemWithCancel;
import com.sofac.fxmharmony.view.customView.LinearLayoutGallery;
import com.sofac.fxmharmony.view.customView.PhotoVideoItemWithCancel;

import java.util.ArrayList;
import java.util.List;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.GET_POST_FILES_END_URL;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class ChangePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postTextInput;
    private PostDTO postDTO;
    private BottomNavigationView bottomNavigationView;

    private LinearLayoutGallery imagesGalleryLayout;
    private LinearLayoutGallery videoGalleryLayout;
    private LinearLayoutGallery fileGalleryLayout;


    private final static int REQUEST_TAKE_FILE = 11111;
    private final static int REQUEST_TAKE_GALLERY_VIDEO = 11112;
    private final static int REQUEST_TAKE_PHOTO = 11113;

    private FxmPostFile fxmPostFile;

    private List<String> videoList;
    private List<String> imageList;
    private List<String> fileList;

    private List<Uri> fileListToSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);
        initUI();


        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Intent intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);
        fxmPostFile = new FxmPostFile(postDTO);

        postTextInput.setText(postDTO.getPostTextOriginal());

        videoList = fxmPostFile.getVideoList();
        imageList = fxmPostFile.getImageList();
        fileList = fxmPostFile.getFileList();

        fileListToSend = new ArrayList<>();

        for (String imgName : imageList) {
            imagesGalleryLayout.addView(new PhotoVideoItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + imgName), imageList, fileListToSend));
        }
        for (String videoName : videoList) {
            videoGalleryLayout.addView(new PhotoVideoItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + videoName), videoList, fileListToSend));
        }
        for (String fileName : fileList) {
            videoGalleryLayout.addView(new FileItemWithCancel(this, Uri.parse(BASE_URL + GET_POST_FILES_END_URL + fileName), fileList, fileListToSend));
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (!PermissionManager.checkPermissionGranted(ChangePost.this, PermissionManager.REQUEST_CAMERA) || !PermissionManager.checkPermissionGranted(ChangePost.this, PermissionManager.REQUEST_STORAGE)) {
                            PermissionManager.verifyCameraPermissions(ChangePost.this);
                            PermissionManager.verifyStoragePermissions(ChangePost.this);
                            return true;
                        } else {
                            switch (item.getItemId()) {


                                case R.id.action_take_photo:

                                    Intent takePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    takePhotoIntent.setType("image/*");
                                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                                    return false;


                                case R.id.action_take_video:

                                    Intent takeVideoIntent = new Intent();
                                    takeVideoIntent.setType("video/*");
                                    takeVideoIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(takeVideoIntent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                                    return false;


                                case R.id.action_take_file:

                                    Intent takeFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                    takeFileIntent.setType("*/*");
                                    startActivityForResult(takeFileIntent, REQUEST_TAKE_FILE);
                                    return false;

                                case R.id.add_files:

                                    showFileUI();
                                    return true;
                            }
                            return true;
                        }
                    }
                });


        hideFileUI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_post, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Log.i("CODE", "" + requestCode);

            if (data != null) {

                Uri fileUri = data.getData();

                for (Uri uriToSend : fileListToSend) {
                    if (fileUri.equals(uriToSend)) return;
                }
                fileListToSend.add(fileUri);

                if (requestCode == REQUEST_TAKE_PHOTO) {

                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, imageList, fileListToSend);
                    imagesGalleryLayout.addView(photoVideoItemWithCancel);

                } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                    PhotoVideoItemWithCancel photoVideoItemWithCancel = new PhotoVideoItemWithCancel(this, fileUri, videoList, fileListToSend);
                    videoGalleryLayout.addView(photoVideoItemWithCancel);

                } else if (requestCode == REQUEST_TAKE_FILE) {

                    FileItemWithCancel fileItemWithCancel = new FileItemWithCancel(this, fileUri, fileList, fileListToSend);
                    fileGalleryLayout.addView(fileItemWithCancel);

                }
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:
                if (!postTextInput.getText().toString().equals("")) {

                    postDTO.setPostTextOriginal(postTextInput.getText().toString());

/*
                    new GroupExchangeOnServer<PostDTO>(new PostDTO(1L, postDTO.getServerID(), preferences.getLong(USER_ID_PREF, 0L), postDTO.getUserName(), null, postDTO.getPostTextOriginal(), null, null, null), true, UPDATE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
                        @Override
                        public void processFinish(Boolean isSuccess) {
                            if (isSuccess) {
                                finish();
                                RequestMethods.startServiceAttachLoadFilesToPost(this, fileListToSend , );
                            }
                        }
                    }).execute();*/


                } else {
                    Toast.makeText(this, "Please input text post", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        hideFileUI();
    }


    private void initUI() {
        setTitle(getString(R.string.change_post_activity_name));
        postTextInput = (EditText) findViewById(R.id.post_text_input);
        imagesGalleryLayout = (LinearLayoutGallery) findViewById(R.id.image_gallery_layout);
        imagesGalleryLayout.setGalleryView((TextView) findViewById(R.id.image_gallery_name));
        videoGalleryLayout = (LinearLayoutGallery) findViewById(R.id.video_gallery_layout);
        videoGalleryLayout.setGalleryView((TextView) findViewById(R.id.video_gallery_name));
        fileGalleryLayout = (LinearLayoutGallery) findViewById(R.id.file_gallery_layout);
        fileGalleryLayout.setGalleryView((TextView) findViewById(R.id.file_gallery_name));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    }

    private void hideFileUI() {

        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.VISIBLE);

    }

    private void showFileUI() {

        bottomNavigationView.findViewById(R.id.action_take_photo).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.action_take_video).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.action_take_file).setVisibility(View.VISIBLE);
        bottomNavigationView.findViewById(R.id.add_files).setVisibility(View.GONE);

    }


}



