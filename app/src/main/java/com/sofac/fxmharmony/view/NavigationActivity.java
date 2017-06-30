package com.sofac.fxmharmony.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.fragment.ContentFragment;
import com.sofac.fxmharmony.view.fragment.GroupFragment;

import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.IS_AUTHORIZATION;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;

public class NavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private static long backPressed;
    private FloatingActionButton floatingActionButton;
    FragmentManager fragmentManager;
    Fragment fragment;
    Class fragmentClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setTitle("Push");

        fragment = null;
        fragmentClass = ContentFragment.class;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivityForResult(new Intent(NavigationActivity.this, CreatePost.class), 1);
            }
        });
        floatingActionButton.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.findViewById(R.id.idNavDrawNameManager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        changeFragment();
        super.onResume();
    }

    //    private void setupViewPager(ViewPager viewPager) {

//         viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 1) {
//                    floatingActionButton.setVisibility(View.VISIBLE);
//                } else {
//                    floatingActionButton.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    public void changeFragment(){
        fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.idFragmentChanger, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Timber.e("!!!!!!");
            return;
        }
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

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.idPushItem:
                fragmentClass = ContentFragment.class;
                floatingActionButton.setVisibility(View.INVISIBLE);
                changeFragment();
                break;
            case R.id.idGroupItem:
                fragmentClass = GroupFragment.class;
                floatingActionButton.setVisibility(View.VISIBLE);
                changeFragment();
                break;
            case R.id.idSettingItem:
//                fragmentClass = ContentFragment.class;
//                floatingActionButton.setVisibility(View.INVISIBLE);
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

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
