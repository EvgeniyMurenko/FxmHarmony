package com.sofac.fxmharmony.view.customView;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;


public class PhotoVideoItemWithCancel extends RelativeLayout {

    private Uri pictureUri;

    private List<String> photoVideoList;
    private List<Uri> photoVideoListToSend;
    private boolean isRemoteVideo;



    public PhotoVideoItemWithCancel(Context context, Uri uri, List<String> photoVideoList, List<Uri> photoVideoListToSend, boolean isRemoteVideo) {
        super(context);

        this.pictureUri = uri;
        this.photoVideoList = photoVideoList;
        this.photoVideoListToSend = photoVideoListToSend;
        this.isRemoteVideo = isRemoteVideo;


        int padding = AppMethods.getPxFromDp(5, context);
        int height = AppMethods.getPxFromDp(100, context);
        int width = AppMethods.getPxFromDp(100, context);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        this.setLayoutParams(layoutParams);
        this.setPadding(padding, padding, padding, padding);

        ImageView imagePhoto = new ImageView(context);

        final Button cancelButton = new Button(context);

        this.addView(imagePhoto);
        this.addView(cancelButton);

        RelativeLayout.LayoutParams imagePhotoLayoutParams = (RelativeLayout.LayoutParams) imagePhoto.getLayoutParams();
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, cancelButton.getId());
        imagePhotoLayoutParams.addRule(RelativeLayout.ALIGN_END, cancelButton.getId());
        imagePhotoLayoutParams.height = AppMethods.getPxFromDp(height, context);
        imagePhotoLayoutParams.width = AppMethods.getPxFromDp(width, context);


        RelativeLayout.LayoutParams cancelButtonLayoutParams = (RelativeLayout.LayoutParams) cancelButton.getLayoutParams();
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        cancelButtonLayoutParams.width = AppMethods.getPxFromDp(30, context);
        cancelButtonLayoutParams.height = AppMethods.getPxFromDp(30, context);

        if (!isRemoteVideo) {
            Glide.with(context)
                    .load(uri)
                    .error(R.drawable.icon_toolbar)
                    .override(height, height)
                    .centerCrop()
                    .into(imagePhoto);
        } else {
            Bitmap videoThumbnail = null;
            try {
                videoThumbnail = AppMethods.retrieveVideoFrameFromVideo(uri.toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            videoThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(context)
                    .load(stream.toByteArray())
                    .error(R.drawable.icon_toolbar)
                    .override(height, height)
                    .centerCrop()
                    .into(imagePhoto);
        }


        cancelButton.setBackground(context.getDrawable(R.drawable.remove_symbol));

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("FILETEST", "ON DISSMISS");

                Iterator<String> photoVideoListIterator = PhotoVideoItemWithCancel.this.photoVideoList.iterator();
                while (photoVideoListIterator.hasNext()) {
                    String photoVideoURL = Constants.BASE_URL + Constants.GET_POST_FILES_END_URL + photoVideoListIterator.next();
                    Log.i("FILETEST", "ORIGINAL" + pictureUri + "  AND " + photoVideoURL);
                    if (photoVideoURL.equals(pictureUri.toString())) {
                        Log.i("FILETEST", "BEFORE files equals REMOVE ACTION");
                        photoVideoListIterator.remove();
                        Log.i("FILETEST", "files equals REMOVE ACTION");
                    }
                }

                Log.i("FILETEST", "VIDEO TO SEND CHECK");
                Iterator<Uri> photoVideoListToSendIterator = PhotoVideoItemWithCancel.this.photoVideoListToSend.iterator();
                while (photoVideoListToSendIterator.hasNext()) {
                    Uri photoVideoUri = photoVideoListToSendIterator.next();
                    if (photoVideoUri.equals(pictureUri)) {
                        photoVideoListToSendIterator.remove();
                        Log.i("FILETEST", "filesToSend equals REMOVE ACTION");
                    }
                }

                Log.i("FILETEST", "FROM SERVER");
                Iterator<String> photoVideoListIterators = PhotoVideoItemWithCancel.this.photoVideoList.iterator();
                while (photoVideoListIterators.hasNext()) {
                    String photoVideoURL = photoVideoListIterators.next();
                    Log.i("FILETEST", "image " + photoVideoURL);
                }

                Log.i("FILETEST", "VIDEO TO SEND CHECK");
                Iterator<Uri> photoVideoListToSendIterators = PhotoVideoItemWithCancel.this.photoVideoListToSend.iterator();
                while (photoVideoListToSendIterators.hasNext()) {
                    Uri photoVideoUri = photoVideoListToSendIterators.next();
                    Log.i("FILETEST", "image " + photoVideoUri);
                }

                Log.i("FILETEST", "OB END");

                LinearLayoutGallery parent = (LinearLayoutGallery) PhotoVideoItemWithCancel.this.getParent();
                parent.removeView(PhotoVideoItemWithCancel.this);


            }
        });

    }


}
