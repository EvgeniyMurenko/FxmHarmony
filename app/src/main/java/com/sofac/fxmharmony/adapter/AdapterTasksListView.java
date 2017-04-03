package com.sofac.fxmharmony.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.MessageTask;
import java.util.ArrayList;

/**
 * Created by Maxim on 03.04.2017.
 */

public class AdapterTasksListView extends BaseAdapter {
    private ArrayList<MessageTask> messageTaskArrayList;
    private Context ctx;
    private LayoutInflater inflater;

    public AdapterTasksListView (Context context, ArrayList<MessageTask> messageTaskArrayList){
        this.messageTaskArrayList = messageTaskArrayList;
        ctx = context;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.idTitleItemTask)).setText(messageTask.getTitle());
        ((TextView) view.findViewById(R.id.idDateItemTask)).setText(messageTask.getDate().toString());
        ((TextView) view.findViewById(R.id.idMessageItemTask)).setText(messageTask.getMessageText());

        return view;
    }

    MessageTask getMessageTask(int position) {
        return ((MessageTask) getItem(position));
    }

}
