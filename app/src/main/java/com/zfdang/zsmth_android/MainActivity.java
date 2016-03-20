package com.zfdang.zsmth_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zfdang.zsmth_android.models.Board;
import com.zfdang.zsmth_android.models.Mail;
import com.zfdang.zsmth_android.models.Topic;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        GuidanceFragment.OnListFragmentInteractionListener,
        FavoriteFragment.OnListFragmentInteractionListener,
        AllBoardFragment.OnListFragmentInteractionListener,
        MailFragment.OnListFragmentInteractionListener
//        SettingFragment.OnFragmentInteractionListener,
//        AboutFragment.OnFragmentInteractionListener
{

    // guidance fragment: display hot topics
    // this fragment is using RecyclerView to show all hot topics
    GuidanceFragment guidanceFragment = null;
    FavoriteFragment favoriteFragment = null;
    AllBoardFragment allBoardFragment = null;
    MailFragment mailFragment = null;

    SettingFragment settingFragment = null;
    AboutFragment aboutFragment = null;

    private ProgressDialog pdialog = null;
    // used by startActivityForResult
    static final int MAIN_ACTIVITY_REQUEST_CODE = 9527;  // The request code

    private ImageView mAvatar = null;
    private TextView mUsername = null;

    private DrawerLayout mDrawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // how to adjust the height of toolbar
        // http://stackoverflow.com/questions/17439683/how-to-change-action-bar-size
        // zsmth_actionbar_size @ dimen ==> ThemeOverlay.ActionBar @ styles ==> theme @ app_bar_main.xml
//        toolbar.setSubtitle("Hello world");
//        toolbar.setLogo(R.mipmap.zsmth);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAvatar = (ImageView) findViewById(R.id.user_avatar);
        mAvatar.setOnClickListener(this);

        mUsername = (TextView) findViewById(R.id.user_name);
        mUsername.setOnClickListener(this);

        // init all fragments
        initFragments();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, guidanceFragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    protected void initFragments() {
        guidanceFragment = new GuidanceFragment();
        favoriteFragment = new FavoriteFragment();
        allBoardFragment = new AllBoardFragment();
        mailFragment = new MailFragment();

        settingFragment = new SettingFragment();
        aboutFragment = new AboutFragment();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // get username from data, then update mUsername
                String username = data.getStringExtra("username");
                if(username != null && username.length() > 0){
                    // update displayed user name
                    mUsername.setText(username);

                    // TODO: update user_avatar
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(String message, final boolean show) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(this);
        }
        if (show) {
            pdialog.setMessage(message);
            pdialog.show();
        } else {
            pdialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast toast = Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        } else if (id == R.id.action_switch_theme) {

        } else if (id == R.id.action_login) {

        } else if (id == R.id.action_logout) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String title = "";

        if (id == R.id.nav_guidance) {
            fragment = guidanceFragment;
            title = "首页导读";
        } else if (id == R.id.nav_favorite) {
            fragment = favoriteFragment;
            title = "收藏夹";
        } else if (id == R.id.nav_all_boards) {
            fragment = allBoardFragment;
            title = "所有版面";
        } else if (id == R.id.nav_mail) {
            fragment = mailFragment;
            title = "邮件";
        } else if (id == R.id.nav_setting) {
            fragment = settingFragment;
            title = "设置";
        } else if (id == R.id.nav_about) {
            fragment = aboutFragment;
            title = "关于";
        }

        // switch fragment
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle("zSMTH - " + title);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_avatar || id == R.id.user_name) {
            // 点击图标或者文字，都弹出登录对话框
            mDrawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onListFragmentInteraction(Topic item) {
        // Guidance Fragment
        Log.d("MainActivity", item.getTitle() + "is clicked");
        Toast.makeText(this, item.getTitle() + " is clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onListFragmentInteraction(Mail item) {
        // MailFragment
        Toast.makeText(this, item.toString() + " is clicked", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onListFragmentInteraction(Board item) {
        // shared by Favorite & allboard
        Toast.makeText(this, item.toString() + " is clicked", Toast.LENGTH_LONG).show();

    }
}