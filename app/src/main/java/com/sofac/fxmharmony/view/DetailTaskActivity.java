package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.DataManager;
import com.sofac.fxmharmony.data.dto.MessageTask;
import com.sofac.fxmharmony.data.dto.StaffInfo;
import com.sofac.fxmharmony.data.dto.base.ServerRequest;
import com.sofac.fxmharmony.data.dto.base.ServerResponse;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.TASK_INFO;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;

public class DetailTaskActivity extends BaseActivity implements View.OnClickListener {
    MessageTask messageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        messageTask = (MessageTask) getIntent().getSerializableExtra(TASK_INFO);
        TextView textView = (TextView) findViewById(R.id.contextView);
        textView.setText(messageTask.getMessageText());

        setTitle(messageTask.getTitle());
        if (messageTask.getApprove()) {
            ((Button) findViewById(R.id.accept_task)).setVisibility(View.INVISIBLE);
        }
        ((Button) findViewById(R.id.accept_task)).setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_task:
                SetReadMessageTaskRequest task = new SetReadMessageTaskRequest();
                task.execute(messageTask.getId());
                finish();
                break;
            default:
                break;
        }
    }


    /* Поток подключения */
    private class SetReadMessageTaskRequest extends AsyncTask<Long, Void, String> {

        ServerResponse<StaffInfo> staffInfoServerResponse;

        @Override
        protected void onPreExecute() {
            //on pre execute
        }

        @Override
        protected String doInBackground(Long... urls) {
            ServerRequest serverRequest = new ServerRequest(Constants.SET_READ_MESSAGE_TASK_REQUEST, urls[0]);
            DataManager dataManager = DataManager.getInstance();
            staffInfoServerResponse = dataManager.sendAuthorizationRequest(serverRequest);

            if (staffInfoServerResponse != null) {
                return staffInfoServerResponse.getResponseStatus();
            }
            return Constants.SERVER_REQUEST_ERROR;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i(result);

            if (result.equals(Constants.REQUEST_SUCCESS)) {
            } else {
                Toast.makeText(DetailTaskActivity.this, R.string.errorConnection, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
