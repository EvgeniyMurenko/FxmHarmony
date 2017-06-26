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

import static com.sofac.fxmharmony.Constants.USER_ID_PREF;


public class CreatePost extends BaseActivity {

    public SharedPreferences preferences;
    private EditText postTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_message);
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);


        postTextInput = (EditText) findViewById(R.id.post_text_input);

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
            case R.id.send_post_button:
                if(!postTextInput.getText().toString().equals("")) {

                    Editable text = postTextInput.getText();

                    //Toast.makeText(this, "id = "+preferences.getLong(USER_ID_PREF,0L), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreatePost.this, MainActivity.class);
                    intent.putExtra("postText" , text.toString());
                    startActivity(intent);



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



