package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;

import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;


public class ChangePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postTextInput;
    private PostDTO postDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_post);

        setTitle(getString(R.string.change_post));
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        Intent intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);

        postTextInput = (EditText) findViewById(R.id.post_text_input);
        postTextInput.setText(postDTO.getPostTextOriginal());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_post_button:
                if (!postTextInput.getText().toString().equals("")) {

                    postDTO.setPostTextOriginal(postTextInput.getText().toString());

                    new GroupExchangeOnServer<PostDTO>(new PostDTO(1L, postDTO.getServerID(), preferences.getLong(USER_ID_PREF,0L), postDTO.getUserName(), null, postDTO.getPostTextOriginal(),null,null,null), true, UPDATE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
                        @Override
                        public void processFinish(Boolean isSuccess) {
                            if (isSuccess) {
                                finish();
                            }
                        }
                    }).execute();

                } else {
                    Toast.makeText(this, R.string.please_input_text_post, Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



