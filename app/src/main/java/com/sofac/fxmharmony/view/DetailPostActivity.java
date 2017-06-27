package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterComentsGroup;
import com.sofac.fxmharmony.data.dto.CommentDTO;


import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.LOAD_COMMENTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;

public class DetailPostActivity extends BaseActivity implements View.OnClickListener {

    Button buttonSend;
    TextView userNamePost;
    TextView datePost;
    TextView messagePost;
    Intent intent;
    View headerView;
    ArrayList<CommentDTO> arrayListComments;
    ListView listViewComments;
    PostDTO postDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        new GroupExchangeOnServer<>(1L, LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {

                List<CommentDTO> commentDTOs  = CommentDTO.listAll(CommentDTO.class);
                for(CommentDTO commentDTO : commentDTOs) {
                    Timber.i(commentDTO.getCommentText());
                }

            }
        }).execute();

        //buttonSend = (Button) findViewById(R.id.send_button);
        //buttonSend.setOnClickListener(this);

        listViewComments = (ListView) findViewById(R.id.idListViewComments);

        intent = getIntent();
        PostDTO postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);


        if (postDTO != null) {
            headerView = createPostView(postDTO.getUserName(), new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()), postDTO.getPostText());
        }
        listViewComments.addHeaderView(headerView);
        ArrayList<CommentDTO> arrayListComments = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);
        listViewComments.setAdapter(new AdapterComentsGroup(this,arrayListComments));
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

    protected void updateViewList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendComment:
                Toast.makeText(this, "!!! Comment add", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
