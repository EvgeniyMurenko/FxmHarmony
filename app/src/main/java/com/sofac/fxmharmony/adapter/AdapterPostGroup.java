package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.PushMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

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
        ((TextView) view.findViewById(R.id.idTitleItemPost)).setText(postDTO.getId().toString());
        ((TextView) view.findViewById(R.id.idDateItemPost)).setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(postDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        ((TextView) view.findViewById(R.id.idMessageItemPost)).setText(Html.fromHtml(postDTO.getPostText()));

        return view;
    }

    PostDTO getPostDTO(int position) {
        return ((PostDTO) getItem(position));
    }

}
