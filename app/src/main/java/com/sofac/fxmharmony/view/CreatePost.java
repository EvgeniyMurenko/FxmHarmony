package com.sofac.fxmharmony.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.util.Date;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class CreatePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_message);
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);


        postMessage = (EditText) findViewById(R.id.post_text_input);

/*        Intent intent = getIntent();
        PushMessage pushMessage = (PushMessage) intent.getSerializableExtra(ONE_PUSH_MESSAGE_DATA);

        if(pushMessage!=null){
            titleDetailPushMessage.setText(pushMessage.getTitle());
            dateDetailPushMessage.setText(pushMessage.getDate());
            messageDetailPushMessage.setText(pushMessage.getMessage());
        }*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case R.id.menu_done:
                if(!postMessage.getText().toString().equals("")) {

                    PostDTO postDTO = new PostDTO(1L, preferences.getLong(USER_ID_PREF,0L),"Name", new Date(), postMessage.getText().toString());

                    Toast.makeText(this, "id = "+preferences.getLong(USER_ID_PREF,0L), Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    Toast.makeText(this, "Please input text message", Toast.LENGTH_SHORT).show();
                }
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



