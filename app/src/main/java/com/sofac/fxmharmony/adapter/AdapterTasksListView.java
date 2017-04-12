package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.MessageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Maxim on 03.04.2017.
 */

public class AdapterTasksListView extends BaseAdapter {
    private ArrayList<MessageTask> messageTaskArrayList;
    private Context ctx;
    private LayoutInflater inflater;

    public AdapterTasksListView(Context context, ArrayList<MessageTask> messageTaskArrayList) {
        this.messageTaskArrayList = messageTaskArrayList;
        this.ctx = context;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageTaskArrayList.get(position);
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
            view = inflater.inflate(R.layout.item_task, parent, false);
        }

        MessageTask messageTask = getMessageTask(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        if (messageTask.getApprove()) {
            (view.findViewById(R.id.itemLayout)).setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            (view.findViewById(R.id.itemLayout)).setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        ((TextView) view.findViewById(R.id.idTitleItemTask)).setText(messageTask.getTitle());
        ((TextView) view.findViewById(R.id.idDateItemTask)).setText(dateFormat.format(messageTask.getDate()));
        ((TextView) view.findViewById(R.id.idMessageItemTask)).setText(Html.fromHtml(messageTask.getMessageText()).toString());

        return view;
    }

    MessageTask getMessageTask(int position) {
        return ((MessageTask) getItem(position));
    }

}
