package com.sofac.fxmharmony.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.adapter.AdapterCommentsGroup;
import com.sofac.fxmharmony.data.dto.CommentDTO;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PermissionDTO;
import com.sofac.fxmharmony.data.dto.PostDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.sofac.fxmharmony.Constants.DELETE_COMMENT_REQUEST;
import static com.sofac.fxmharmony.Constants.DELETE_POST_REQUEST;
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
    public Intent intentChangePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        setTitle(getString(R.string.FXM_group));

        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        intentChangePost = new Intent(this, ChangePost.class);
        intent = getIntent();
        postDTO = (PostDTO) intent.getSerializableExtra(ONE_POST_DATA);


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
                                            new GroupExchangeOnServer<>(DetailPostActivity.idComment, true, DELETE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                                                @Override
                                                public void processFinish(Boolean isSuccess , String answer) {
                                                    if (isSuccess) {
                                                        updateListView();
                                                        Toast.makeText(DetailPostActivity.this, R.string.comment_was_delete, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPostActivity.this, R.string.problem_with_ID_comment, Toast.LENGTH_SHORT).show();
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
                        new GroupExchangeOnServer<>(new CommentDTO(1L, null, (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getServerID()), true, WRITE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                            @Override
                            public void processFinish(Boolean isSuccess , String answer) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                } else {
                                    Toast.makeText(DetailPostActivity.this, R.string.create_comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    } else { // Редактирование коментария
                        new GroupExchangeOnServer<>(new CommentDTO(1L, commentDTO.getServerID(), (getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L)), "Name", null, editTextComment.getText().toString(), postDTO.getServerID()), true, UPDATE_COMMENT_REQUEST, DetailPostActivity.this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                            @Override
                            public void processFinish(Boolean isSuccess , String answer) {
                                if (isSuccess) {
                                    updateListView();
                                    editTextComment.setText("");
                                    isCreatingComment = true;
                                } else {
                                    Toast.makeText(DetailPostActivity.this, R.string.edit_comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    }

                } else {
                    Toast.makeText(DetailPostActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initialHeaderPost();
    }

    public void initialHeaderPost() {
        if (postDTO != null) {
            headerView = createPostView(postDTO.getUserName(), new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.GERMAN).format(postDTO.getDate()), postDTO.getPostTextOriginal().replaceAll("<(.*?)>", " "));
            Spinner spinnerLanguage = (Spinner) headerView.findViewById(R.id.spinner_language);

            ArrayList<String> stringsSpinnerLanguage = new ArrayList<>();
            if (postDTO.getPostTextEn() != null && !postDTO.getPostTextEn().isEmpty())
                stringsSpinnerLanguage.add(getString(R.string.english_spinner));
            if (postDTO.getPostTextKo() != null && !postDTO.getPostTextKo().isEmpty())
                stringsSpinnerLanguage.add(getString(R.string.korean_spinner));
            if (postDTO.getPostTextRu() != null && !postDTO.getPostTextRu().isEmpty())
                stringsSpinnerLanguage.add(getString(R.string.russian_spinner));
            if (!stringsSpinnerLanguage.isEmpty())
                stringsSpinnerLanguage.add(0, getString(R.string.original_spinner));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringsSpinnerLanguage);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerLanguage.setAdapter(adapter);
            if (stringsSpinnerLanguage.isEmpty()) spinnerLanguage.setVisibility(View.INVISIBLE);
            listViewComments.addHeaderView(headerView);

            spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (parent.getSelectedItem().toString() == getString(R.string.original_spinner)) {
                        ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(postDTO.getPostTextOriginal());
                        //Toast.makeText(DetailPostActivity.this, "Original", Toast.LENGTH_SHORT).show();

                    } else if (parent.getSelectedItem().toString() == getString(R.string.english_spinner)) {
                        ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(postDTO.getPostTextEn());
                        //Toast.makeText(DetailPostActivity.this, "English", Toast.LENGTH_SHORT).show();

                    } else if (parent.getSelectedItem().toString() == getString(R.string.korean_spinner)) {
                        ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(postDTO.getPostTextKo());
                        //Toast.makeText(DetailPostActivity.this, "Korean", Toast.LENGTH_SHORT).show();

                    } else if (parent.getSelectedItem().toString() == getString(R.string.russian_spinner)) {
                        ((TextView) headerView.findViewById(R.id.idMessagePost)).setText(postDTO.getPostTextRu());
                        //Toast.makeText(DetailPostActivity.this, "Russian", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
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
        new GroupExchangeOnServer<>(postDTO.getServerID(), true, LOAD_COMMENTS_REQUEST, this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
            @Override
            public void processFinish(Boolean output , String answer) {
                arrayListComments = (ArrayList<CommentDTO>) CommentDTO.listAll(CommentDTO.class);
                adapterCommentsGroup = new AdapterCommentsGroup(DetailPostActivity.this, arrayListComments);

                listViewComments.setAdapter(adapterCommentsGroup);
                adapterCommentsGroup.notifyDataSetChanged();
            }
        }).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PermissionDTO permissionDTO = PermissionDTO.findById(PermissionDTO.class, getSharedPreferences(USER_SERVICE, MODE_PRIVATE).getLong(USER_ID_PREF, 1L));
        if (permissionDTO.getTranslatePermission() == null && permissionDTO.getTranslatePermission() && postDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L)|| permissionDTO.getSuperAdminPermission()) {
            getMenuInflater().inflate(R.menu.menu_detail_post, menu);
            getMenuInflater().inflate(R.menu.menu_detail_post_translation, menu);
        } else if (postDTO.getUserID() == preferences.getLong(USER_ID_PREF, 0L)|| permissionDTO.getSuperAdminPermission()) {
            getMenuInflater().inflate(R.menu.menu_detail_post, menu);
        } else if (permissionDTO.getTranslatePermission() == null && permissionDTO.getTranslatePermission()) {
            getMenuInflater().inflate(R.menu.menu_detail_post_translation, menu);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_edit:
                intentChangePost.putExtra(ONE_POST_DATA, postDTO);
                startActivity(intentChangePost);
                return true;
            case R.id.menu_delete:
                new GroupExchangeOnServer<>(postDTO.getServerID(), true, DELETE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponseWithAnswer() {
                    @Override
                    public void processFinish(Boolean isSuccess , String answer) {
                        if (isSuccess) {
                            Toast.makeText(DetailPostActivity.this, R.string.post_was_delete, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).execute();
                return true;
            case R.id.menu_translate_post:
                Intent intentTranslatePost = new Intent(this, TranslatePost.class);
                intentTranslatePost.putExtra(ONE_POST_DATA, postDTO);
                startActivityForResult(intentTranslatePost, 1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            postDTO = (PostDTO) getIntent().getSerializableExtra(ONE_POST_DATA);
            initialHeaderPost();
            updateListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
