package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sofac.fxmharmony.R;

public class WritePostActivity extends AppCompatActivity {


    ImageButton sendPostButton;
    EditText postTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        postTextInput = (EditText) findViewById(R.id.post_text_input);
        sendPostButton = (ImageButton) findViewById(R.id.send_post_button);


        sendPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = postTextInput.getText();

                Intent intent = new Intent(WritePostActivity.this, MainActivity.class);
                intent.putExtra("postText" , text.toString());
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WritePostActivity.this, MainActivity.class));
    }


}
