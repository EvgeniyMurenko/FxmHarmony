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
import com.sofac.fxmharmony.data.dto.PostDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class AdapterCommentsGroup extends BaseAdapter {
    private ArrayList<CommentDTO> commentDTOArrayList;
    private Context ctx;
    private LayoutInflater inflater;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_comment, parent, false);
        }

        CommentDTO commentDTO = getCommentDTO(position);
        ((TextView) view.findViewById(R.id.idNameUserComment)).setText(commentDTO.getUserName());
        ((TextView) view.findViewById(R.id.idDateComment)).setText(new SimpleDateFormat("d MMM yyyy HH:mm", Locale.GERMAN).format(commentDTO.getDate())); //"d MMM yyyy HH:mm:ss"
        ((TextView) view.findViewById(R.id.idMessageItemComment)).setText(Html.fromHtml(commentDTO.getCommentText()));

        return view;
    }

    CommentDTO getCommentDTO(int position) {
        return ((CommentDTO) getItem(position));
    }

}
