package com.sofac.fxmharmony.adapter;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.util.AppMethods;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import timber.log.Timber;

public class AdapterPostGroup extends BaseAdapter {
    private ArrayList<PostDTO> postDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;

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

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_post, parent, false);
        }
        PostDTO postDTO = getPostDTO(position);

        Uri uri = Uri.parse(Constants.BASE_URL + Constants.PART_URL_FILE + postDTO.getPostUserAvatarImage());
        ImageView avatar = (ImageView) view.findViewById(R.id.idAvatarPostItem);
        Glide.with(ctx)
                .load(uri)
                .override(AppMethods.getPxFromDp(50, ctx),AppMethods.getPxFromDp(50, ctx))
                .error(R.drawable.no_avatar)
                .placeholder(R.drawable.no_avatar)
                .bitmapTransform(new CropCircleTransformation(ctx))
                .into(avatar);
        //SimpleDraweeView avatar = (SimpleDraweeView) view.findViewById(R.id.idAvatarPostItem);

        Timber.e("!!!!!!!!!!!! AVATAR URI !!!!!!! "+uri.toString());
        //avatar.setImageURI(uri);

        ((TextView) view.findViewById(R.id.idTitleItemPost)).setText(postDTO.getUserName());
        ((TextView) view.findViewById(R.id.idDateItemPost)).setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(postDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        if(postDTO.getPostTextOriginal()!=null)((TextView) view.findViewById(R.id.idMessageItemPost)).setText(postDTO.getPostTextOriginal().replaceAll("<(.*?)>"," "));
        return view;
    }

    PostDTO getPostDTO(int position) {
        return ((PostDTO) getItem(position));
    }

}
