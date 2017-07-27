package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.util.FileLoadingListener;
import com.sofac.fxmharmony.util.FileLoadingTask;
import com.sofac.fxmharmony.view.DetailPostActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.GET_POST_FILES_END_URL;
import static com.sofac.fxmharmony.Constants.PART_URL_FILE_IMAGE_POST;

public class AdapterPostGroup extends BaseAdapter {
    private ArrayList<PostDTO> postDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;
    DiscreteScrollView discreteScrollView;

    public AdapterPostGroup(Context context, ArrayList<PostDTO> postDTOArrayList) {
        Collections.sort(postDTOArrayList, new Comparator<PostDTO>() {
            public int compare(PostDTO o1, PostDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        this.postDTOArrayList = postDTOArrayList;
        this.ctx = context;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return postDTOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return postDTOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PostDTO postDTO = getPostDTO(position);
        ArrayList<String> listImage = new ArrayList<>();

        View view = inflater.inflate(R.layout.item_post, parent, false);

        LinearLayout linearLayoutFiles = (LinearLayout) view.findViewById(R.id.idListFilesPostItem);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (null != postDTO.getLinksFile() && !"".equals(postDTO.getLinksFile()) && postDTO.getLinksFile().length() > 5) {
            Timber.e("!!!!!!!!!!!!!!!!!" + postDTO.getLinksFile());
            for (final String imageName : postDTO.getLinksFile().split(";#")) {
                View fileItemView = inflater.inflate(R.layout.item_preview_post_file, null);
                TextView textView = (TextView) fileItemView.findViewById(R.id.idNameFile);
                textView.setText(imageName);
                linearLayoutFiles.addView(fileItemView, lParams);
            }
        } else {
            linearLayoutFiles.setVisibility(View.INVISIBLE);
        }


//        discreteScrollView = (DiscreteScrollView) view.findViewById(R.id.idImageCarousel);
//        discreteScrollView.setOrientation(Orientation.HORIZONTAL);
//        discreteScrollView.setOnClickListener(null);
//        discreteScrollView.setOnLongClickListener(null);
//
//        if (null != postDTO.getLinksImage() && !"".equals(postDTO.getLinksImage()) && postDTO.getLinksImage().length() > 5) {
//            for (String imageName : postDTO.getLinksImage().split(";#")) {
//                listImage.add(BASE_URL + PART_URL_FILE_IMAGE_POST + imageName);
//            }
//            Timber.e("!!!!! listImage.toString() !!!! " + listImage.toString());
//            discreteScrollView.setAdapter(new AdapterGalleryGroup(listImage));
//        } else {
//            discreteScrollView.setVisibility(View.INVISIBLE);
//        }

        Uri uri = Uri.parse(BASE_URL + Constants.PART_URL_FILE_AVATAR + postDTO.getPostUserAvatarImage());
        ImageView avatar = (ImageView) view.findViewById(R.id.idAvatarPostItem);
        Glide.with(ctx)
                .load(uri)
                .override(AppMethods.getPxFromDp(50, ctx), AppMethods.getPxFromDp(50, ctx))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .into(avatar);

        ((TextView) view.findViewById(R.id.idTitleItemPost)).setText(postDTO.getUserName());
        ((TextView) view.findViewById(R.id.idDateItemPost)).setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(postDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        if (postDTO.getPostTextOriginal() != null)
            ((TextView) view.findViewById(R.id.idMessageItemPost)).setText(postDTO.getPostTextOriginal().replaceAll("<(.*?)>", " "));
        return view;
    }

    PostDTO getPostDTO(int position) {
        return ((PostDTO) getItem(position));
    }

}
