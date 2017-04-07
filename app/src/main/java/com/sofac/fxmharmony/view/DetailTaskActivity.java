package com.sofac.fxmharmony.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.MessageTask;

import static com.sofac.fxmharmony.Constants.TASK_INFO;

public class DetailTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        MessageTask messageTask = (MessageTask) getIntent().getSerializableExtra(TASK_INFO);
        TextView textView = (TextView) findViewById(R.id.contextView);
        textView.setText(messageTask.getMessageText());

        setTitle(messageTask.getTitle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_datail_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
