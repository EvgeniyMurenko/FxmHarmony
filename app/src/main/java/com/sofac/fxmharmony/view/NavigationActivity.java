package com.sofac.fxmharmony.view;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.util.AppMethods;
import com.sofac.fxmharmony.view.fragment.ContentFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;


import timber.log.Timber;
import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.AVATAR_IMAGE_SIZE;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;


public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private static long backPressed;
    private ImageView avatarImage;
    private TextView userName;
    ContentFragment contentFragment;
    GroupFragment groupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        setTitle(getString(R.string.push_tile));

        contentFragment = new ContentFragment();
        groupFragment = new GroupFragment();
        changerFragment(contentFragment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.findViewById(R.id.idNavDrawNameManager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        avatarImage = (ImageView) header.findViewById(R.id.navAvatarImage);
        userName = (TextView) header.findViewById(R.id.idNavDrawNameManager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppMethods.putAvatarIntoImageView(this, avatarImage);
        userName.setText(AppMethods.getUserName(this));

    }

    public void changerFragment(Fragment fragment){
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.idFragmentChanger, fragment);
        fTrans.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Timber.e("!!!!!!");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.idPushItem:
                changerFragment(contentFragment);
                setTitle(item.getTitle());
                item.setChecked(true);
                break;
            case R.id.idGroupItem:
                changerFragment(groupFragment);
                setTitle(item.getTitle());
                item.setChecked(true);
                break;
            case R.id.idSettingItem:
                Intent intentSettings = new Intent(NavigationActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.idExitItem:
                Intent intentSplashActivity = new Intent(this, SplashActivity.class);
                SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_AUTHORIZATION, false);
                editor.apply();
                intentSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editor.commit();
                startActivity(intentSplashActivity);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ToastLogOut), Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }

}
