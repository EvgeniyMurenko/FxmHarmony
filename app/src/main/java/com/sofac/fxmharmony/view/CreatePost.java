package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;

import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;


public class CreatePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postTextInput;
    TabHost.TabSpec tabSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_message);

        setTitle("Create post");
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

        postTextInput = (EditText) findViewById(R.id.post_text_input);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:
                if(!postTextInput.getText().toString().equals("")) {

                    Editable text = postTextInput.getText();

                    new GroupExchangeOnServer<PostDTO>(new PostDTO(1L, 1L, preferences.getLong(USER_ID_PREF,0L), "Name", null, text.toString(),null,null,null),true, WRITE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
                        @Override
                        public void processFinish(Boolean isSuccess) {
                            Intent intent = new Intent(CreatePost.this, NavigationActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).execute();

                }else{
                    Toast.makeText(this, "Please input text message", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



