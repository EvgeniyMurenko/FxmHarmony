package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.PushMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.R.id.dateDetailPushMessage;
import static com.sofac.fxmharmony.R.id.messageDetailPushMessage;
import static com.sofac.fxmharmony.R.id.titleDetailPushMessage;

public class DetailPostActivity extends BaseActivity {

    TextView userNamePost;
    TextView datePost;
    TextView messagePost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        userNamePost = (TextView) findViewById(R.id.idNamePost);
        datePost = (TextView) findViewById(R.id.idDatePost);
        messagePost = (TextView) findViewById(R.id.idMessagePost);

        Intent intent = getIntent();
        PostDTO postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);

        if(postDTO!=null){
            userNamePost.setText(postDTO.getId()+"");
            datePost.setText(new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()));
            messagePost.setText(postDTO.getPostText());
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
