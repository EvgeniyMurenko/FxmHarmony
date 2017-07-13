package com.sofac.fxmharmony.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Locale;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.LOAD_COMMENTS_REQUEST;
import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_COMMENT_REQUEST;

public class DetailPostActivity extends AppCompatActivity {

    Button buttonSend;
    Intent intent;
    View headerView;
    ArrayList<CommentDTO> arrayListComments;
    ListView listViewComments;
    PostDTO postDTO;
    EditText editTextComment;
    AdapterCommentsGroup adapterCommentsGroup;
    SharedPreferences preferences;
    public static Long idComment = 0L;
    public static Boolean isCreatingComment = true;
    public static CommentDTO commentDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);
        setTitle("FXM group");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Toast.makeText(this, getSupportActionBar().getTitle().toString(), Toast.LENGTH_SHORT).show();

        intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);
        if (postDTO != null) {
            headerView = createPostView(postDTO.getUserName(), new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()), postDTO.getPostTextOriginal().replaceAll("<(.*?)>", " "));
        }

        buttonSend = (Button) findViewById(R.id.sendComment);
        editTextComment = (EditText) findViewById(R.id.edit_text_comment);
        listViewComments = (ListView) findViewById(R.id.idListViewComments);

        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if (arrayListComments != null) {
                    if (position > 0) {
                        editTextComment.append(arrayListComments.get(position - 1).getUserName() + ", ");
                    }
                }
            }
        });

        listViewComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    commentDTO = arrayListComments.get(position - 1);
                    if (commentDTO.getServerID() != null) {
                        DetailPostActivity.idComment = commentDTO.getServerID();
                        if (commentDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailPostActivity.this);
                            builder.setItems(R.array.choice_double_click_post, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which) {
                                        case 0: //Edit
                                            editTextComment.setText("");
                                            editTextComment.append(commentDTO.getCommentText());
                                            isCreatingComment = false;
                                            break;
                                        case 1: //Delete
                                            new GroupExchangeOnServer<>(DetailPostActivity.idComment, true, DELETE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponse() {
                                                @Override
                                                public void processFinish(Boolean isSuccess) {
                                                    if (isSuccess) {
                                                        updateListView();
                                                        Toast.makeText(DetailPostActivity.this, "Comment was delete!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).execute();
                                            break;
                                    }
                                }
                            });
                            builder.show();
                        }
                    } else {
                        Toast.makeText(DetailPostActivity.this, "Problem with ID comments!", Toast.LENGTH_SHORT).show();
                    }
                }


                return true;
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editTextComment.getText().toString()).isEmpty()) {
                    if (isCreatingComment) { //Создание коментария
                        new GroupExchangeOnServer<>(new CommentDTO(1L, null, (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getServerID()), true, WRITE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponse() {
                            @Override
                            public void processFinish(Boolean isSuccess) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                } else {
                                    Toast.makeText(DetailPostActivity.this, "Create comment error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    } else { // Редактирование коментария
                        new GroupExchangeOnServer<>(new CommentDTO(1L, commentDTO.getServerID(), (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getServerID()), true, UPDATE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponse() {
                            @Override
                            public void processFinish(Boolean isSuccess) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                    isCreatingComment = true;
                                } else {
                                    Toast.makeText(DetailPostActivity.this, "Edit comment error!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    }

                } else {
                    Toast.makeText(DetailPostActivity.this, "Field empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listViewComments.addHeaderView(headerView);
    }

    View createPostView(String name, String date, String message) {

        View v = getLayoutInflater().inflate(R.layout.post_view_detail, null);

        ((TextView) v.findViewById(R.id.idNamePost)).setText(name);
        ((TextView) v.findViewById(R.id.idDatePost)).setText(date);
        ((TextView) v.findViewById(R.id.idMessagePost)).setText(message);

        return v;
    }

    @Override
    protected void onResume() {
        updateListView();
        super.onResume();
    }

    public void updateListView() {
        new GroupExchangeOnServer<>(postDTO.getServerID(), true, LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
            @Override
            public void processFinish(Boolean output) {
                arrayListComments = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);
                adapterCommentsGroup = new AdapterCommentsGroup(DetailPostActivity.this, arrayListComments);
                listViewComments.setAdapter(adapterCommentsGroup);
                adapterCommentsGroup.notifyDataSetChanged();
            }
        }).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_edit:
                Toast.makeText(this, "menu_edit", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                Toast.makeText(this, "menu_delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
