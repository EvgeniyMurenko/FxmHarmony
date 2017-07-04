package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PushMessage;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;

public class DetailPushMessageActivity extends NavigationActivity {

    TextView titleDetailPushMessage;
    TextView dateDetailPushMessage;
    TextView messageDetailPushMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_push_message);

        titleDetailPushMessage = (TextView) findViewById(R.id.titleDetailPushMessage);
        dateDetailPushMessage = (TextView) findViewById(R.id.dateDetailPushMessage);
        messageDetailPushMessage = (TextView) findViewById(R.id.messageDetailPushMessage);

        Intent intent = getIntent();
        PushMessage pushMessage = (PushMessage) intent.getSerializableExtra(ONE_PUSH_MESSAGE_DATA);

        if(pushMessage!=null){
            titleDetailPushMessage.setText(pushMessage.getTitle());
            dateDetailPushMessage.setText(pushMessage.getDate());
            messageDetailPushMessage.setText(pushMessage.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_push_message, menu);
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
