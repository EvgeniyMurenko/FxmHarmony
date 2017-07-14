package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.GroupExchangeOnServer;
import com.sofac.fxmharmony.data.dto.PostDTO;

import static com.sofac.fxmharmony.Constants.ONE_POST_DATA;
import static com.sofac.fxmharmony.Constants.UPDATE_POST_REQUEST;
import static com.sofac.fxmharmony.Constants.USER_ID_PREF;
import static com.sofac.fxmharmony.Constants.WRITE_POST_REQUEST;


public class TranslatePost extends BaseActivity {

    public SharedPreferences preferences;
    public TextView postTextOrig;
    public EditText postTextEng;
    public EditText postTextKor;
    public EditText postTextRus;
    TabHost.TabSpec tabSpec;
    PostDTO postDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_post);
        setTitle("Translate post");

        postDTO = (PostDTO) getIntent().getSerializableExtra(ONE_POST_DATA);
        assert (postDTO == null);
        preferences = getSharedPreferences(USER_SERVICE, MODE_PRIVATE);

        //TextView
        postTextOrig = (TextView) findViewById(R.id.id_text_orig);
        if (postDTO.getPostTextOriginal() != null && !"".equals(postDTO.getPostTextOriginal()))
            postTextOrig.setText(postDTO.getPostTextOriginal().replaceAll("<(.*?)>", " "));

        //EditText
        postTextEng = (EditText) findViewById(R.id.id_text_eng);
        if (postDTO.getPostTextEn() != null && !"".equals(postDTO.getPostTextEn()))
            postTextEng.setText(postDTO.getPostTextEn());

        postTextKor = (EditText) findViewById(R.id.id_text_kor);
        if (postDTO.getPostTextKo() != null && !"".equals(postDTO.getPostTextKo()))
            postTextKor.setText(postDTO.getPostTextKo());

        postTextRus = (EditText) findViewById(R.id.id_text_rus);
        if (postDTO.getPostTextRu() != null && !"".equals(postDTO.getPostTextRu()))
            postTextRus.setText(postDTO.getPostTextRu());

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        tabHost.setup();

        tabSpec = tabHost.newTabSpec("eng");
        tabSpec.setIndicator(getString(R.string.text_eng));
        tabSpec.setContent(R.id.tab_eng);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("kor");
        tabSpec.setIndicator(getString(R.string.text_kor));
        tabSpec.setContent(R.id.tab_kor);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("rus");
        tabSpec.setIndicator(getString(R.string.text_rus));
        tabSpec.setContent(R.id.tab_rus);
        tabHost.addTab(tabSpec);

        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
        }

        // Вкладка по умолчанию активна
        tabHost.setCurrentTabByTag("eng");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {

            }
        });

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

                PostDTO changePostDTO = new PostDTO(1L, postDTO.getServerID(), preferences.getLong(USER_ID_PREF, 0L), postDTO.getUserName(), null, postDTO.getPostTextOriginal(),
                        postTextRus.getText().toString(), postTextEng.getText().toString(), postTextKor.getText().toString());

                new GroupExchangeOnServer<PostDTO>(changePostDTO, true, UPDATE_POST_REQUEST, this, new GroupExchangeOnServer.AsyncResponse() {
                    @Override
                    public void processFinish(Boolean isSuccess) {
                        Intent intent = new Intent(TranslatePost.this, DetailPostActivity.class);
                        intent.putExtra(ONE_POST_DATA, postDTO);
                        setResult(2, intent);
                        finish();
                    }
                }).execute();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



