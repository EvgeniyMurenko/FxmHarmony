package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.CommentDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class AdapterCommentGroup extends BaseAdapter {

    private ArrayList<CommentDTO> commentDTOs;
    private Context context;
    private LayoutInflater inflater;

    public AdapterCommentGroup(Context context, ArrayList<CommentDTO> commentDTOs) {

        Collections.sort(commentDTOs, new Comparator<CommentDTO>() {
            public int compare(CommentDTO o1, CommentDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        this.commentDTOs = commentDTOs;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return commentDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return commentDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_comment, parent, false);
        }

        CommentDTO commentDTO = getCommentDTO(position);
        ((TextView) view.findViewById(R.id.idNamePost)).setText(commentDTO.getUserName());
        ((TextView) view.findViewById(R.id.idDateItemPost)).setText(new SimpleDateFormat("d MMM yyyy", Locale.GERMAN).format(commentDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        ((TextView) view.findViewById(R.id.idMessageItemPost)).setText(Html.fromHtml(commentDTO.getCommentText()));

        return view;
    }

    CommentDTO getCommentDTO(int position) {
        return ((CommentDTO) getItem(position));
    }

}
