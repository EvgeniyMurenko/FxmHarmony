package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCommentGroup;
import com.sofac.fxmharmony.adapter.AdapterPostGroup;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.LOAD_COMMENTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.ONE_PUSH_MESSAGE_DATA;
import static com.sofac.fxmharmony.R.id.dateDetailPushMessage;
import static com.sofac.fxmharmony.R.id.messageDetailPushMessage;
import static com.sofac.fxmharmony.R.id.titleDetailPushMessage;

public class DetailPostActivity extends BaseActivity {

    TextView userNamePost;
    TextView datePost;
    TextView messagePost;

    Intent intentDetailPostActivity;
    AdapterCommentGroup adapterCommentGroup;
    ListView listViewComment;
    ArrayList<CommentDTO> commentDTOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        userNamePost = (TextView) findViewById(R.id.idNamePost);
        datePost = (TextView) findViewById(R.id.idDatePost);
        messagePost = (TextView) findViewById(R.id.idMessagePost);
        listViewComment = (ListView) findViewById(R.id.commentListView);



        Intent intent = getIntent();
        PostDTO postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);

        if(postDTO!=null){
            userNamePost.setText(postDTO.getId()+"");
            datePost.setText(new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()));
            messagePost.setText(postDTO.getPostText());
        }


        new GroupExchangeOnServer<>(new Long(1), LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {

                List<CommentDTO> commentDTOs  = CommentDTO.listAll(CommentDTO.class);
                for(CommentDTO commentDTO : commentDTOs) {
                    Timber.i(commentDTO.getCommentText());
                }

            }
        }).execute();

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

        commentDTOs = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);

        if (commentDTOs != null) {
            adapterCommentGroup = new AdapterCommentGroup(this, commentDTOs);
        /*    this.setListAdapter(adapterPostGroup);*/
        }

/*
        listViewComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (postDTOs != null) {
                    intentDetailPostActivity.putExtra(ONE_POST_DATA, postDTOs.get(position));
                    startActivity(intentDetailPostActivity);
                }
            }
        });
*/



    }
}
