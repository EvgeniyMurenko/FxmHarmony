package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.type.Color;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.util.AppMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.sofac.fxmharmony.view.fragment.GroupFragment.postDTO;

public class AdapterCommentsGroup extends BaseAdapter {
    private ArrayList<CommentDTO> commentDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;
    /*private View view;
    private TextView translateTextView;
    private TextView textMessageLink;
    private LinearLayout linearLayout;*/

    public AdapterCommentsGroup(Context context, ArrayList<CommentDTO> commentDTOArrayList) {
        Collections.sort(commentDTOArrayList, new Comparator<CommentDTO>() {
            public int compare(CommentDTO o1, CommentDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        this.commentDTOArrayList = commentDTOArrayList;
        this.ctx = context;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return commentDTOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentDTOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // используем созданные, но не используемые view
        //View view = convertView;
        //if (view == null) {
        final View view = inflater.inflate(R.layout.item_comment, parent, false);
        //}
        final CommentDTO commentDTO = getCommentDTO(position);

        Uri uri = Uri.parse(Constants.BASE_URL + Constants.PART_URL_FILE_AVATAR + commentDTO.getCommentUserAvatarImage());
        ImageView avatar = (ImageView) view.findViewById(R.id.idCommentAvatar);
        Glide.with(ctx)
                .load(uri)
                .override(AppMethods.getPxFromDp(35, ctx), AppMethods.getPxFromDp(35, ctx))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .into(avatar);

        //Translate comment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TranslateOptions options = TranslateOptions.newBuilder().setApiKey(Constants.CLOUD_API_KEY).build();
        Translate translate = options.getService();
        final Translation translation = translate.translate((commentDTO.getCommentText()).replaceAll("<(.*?)>", " "), Translate.TranslateOption.targetLanguage(Locale.getDefault().getLanguage()));

        //String postComment = commentDTO.getCommentText().replaceAll("<(.*?)>", "");
        ((TextView) view.findViewById(R.id.idNameUserComment)).setText(commentDTO.getUserName());
        ((TextView) view.findViewById(R.id.idDateComment)).setText(new SimpleDateFormat("d MMM yyyy HH:mm", Locale.GERMAN).format(commentDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        ((TextView) view.findViewById(R.id.idMessageItemComment)).setText(commentDTO.getCommentText().replaceAll("<(.*?)>", ""));//postDTO.getPostTextOriginal().replaceAll("<(.*?)>"," ")  translation.getTranslatedText()


        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.commentLiner);

        final TextView translateTextView = new TextView(view.getContext());

        final TextView textMessageLink = new TextView(view.getContext());
        textMessageLink.setText("Translate");
        textMessageLink.setTextColor(view.getResources().getColor(R.color.commentTranslate));
        final Drawable drawable = view.getResources().getDrawable(R.drawable.verticalline);


        linearLayout.addView(textMessageLink);


        textMessageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.removeView(textMessageLink);
                linearLayout.addView(translateTextView);
                translateTextView.setText(translation.getTranslatedText());
                linearLayout.setBackground(drawable);
            }
        });

        return view;
    }

    CommentDTO getCommentDTO(int position) {
        return ((CommentDTO) getItem(position));
    }

}
