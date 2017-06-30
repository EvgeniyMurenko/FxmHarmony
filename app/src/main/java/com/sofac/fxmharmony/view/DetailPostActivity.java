package com.sofac.fxmharmony.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCommentsGroup;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.LOAD_COMMENTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_COMMENT_REQUEST;

public class DetailPostActivity extends BaseActivity {

    Button buttonSend;
    Intent intent;
    View headerView;
    ArrayList<CommentDTO> arrayListComments;
    ListView listViewComments;
    PostDTO postDTO;
    EditText editTextComment;
    AdapterCommentsGroup adapterCommentsGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);

        if (postDTO != null) {
            headerView = createPostView(postDTO.getUserName(), new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()), postDTO.getPostText());
        }

        buttonSend = (Button) findViewById(R.id.sendComment);
        editTextComment = (EditText) findViewById(R.id.edit_text_comment);
        listViewComments = (ListView) findViewById(R.id.idListViewComments);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(editTextComment.getText().toString())) {
                    new GroupExchangeOnServer<>(new CommentDTO(1L, (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)),"Name", null, editTextComment.getText().toString(), postDTO.getServerID()), true, WRITE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponse() {
                        @Override
                        public void processFinish(Boolean isSuccess) {
                            updateListView();
                            editTextComment.setText("");
                        }
                    }).execute();
                } else {
                    Toast.makeText(DetailPostActivity.this, "Field empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listViewComments.addHeaderView(headerView);
    }

    @Override
    protected void onResume() {
        updateListView();
        super.onResume();
    }

    public void updateListView() {
        new GroupExchangeOnServer<>(postDTO.getServerID(),true, LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                arrayListComments = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);
                adapterCommentsGroup = new AdapterCommentsGroup(DetailPostActivity.this, arrayListComments);
                listViewComments.setAdapter(adapterCommentsGroup);
                adapterCommentsGroup.notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }).execute();
    }

    View createPostView(String name, String date, String message) {

        View v = getLayoutInflater().inflate(R.layout.post_view_detail, null);

        ((TextView) v.findViewById(R.id.idNamePost)).setText(name);
        ((TextView) v.findViewById(R.id.idDatePost)).setText(date);
        ((TextView) v.findViewById(R.id.idMessagePost)).setText(message);

        return v;
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
