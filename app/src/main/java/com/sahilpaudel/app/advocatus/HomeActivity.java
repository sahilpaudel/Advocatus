package com.sahilpaudel.app.advocatus;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sahilpaudel.app.advocatus.facebook.SharedPrefFacebook;

import com.sahilpaudel.app.advocatus.fragments.HomeFragment;
import com.sahilpaudel.app.advocatus.fragments.MyFriendFragment;
import com.sahilpaudel.app.advocatus.fragments.MyProfileFragment;
import com.sahilpaudel.app.advocatus.fragments.MyRequestFragment;
import com.sahilpaudel.app.advocatus.fragments.PendingRequestFragment;
import com.sahilpaudel.app.advocatus.fragments.WriteRequestFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView mProfileImage;
    TextView mUserName;
    TextView mUserMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new HomeFragment();

        if(fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFragment, fragment);
            transaction.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, SharedPrefFacebook.getmInstance(HomeActivity.this).getUserInfo().get(3), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //to find the view in the menu layout.
        View navHeader;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        //setting user profile picture.
        String imageUrl = "https://graph.facebook.com/" + SharedPrefFacebook.getmInstance(this).getUserInfo().get(3) + "/picture?type=large";
        mProfileImage = (ImageView)navHeader.findViewById(R.id.iv_userPicture);
        Picasso.with(this).load(imageUrl).into(mProfileImage);

        //fetching user name
        mUserName = (TextView)navHeader.findViewById(R.id.textUserName);
        String fname = SharedPrefFacebook.getmInstance(this).getUserInfo().get(0);
        String lname = SharedPrefFacebook.getmInstance(this).getUserInfo().get(1);
        mUserName.setText(fname+" "+lname);

        //fetching user mail
        mUserMail = (TextView) navHeader.findViewById(R.id.textUserMail);
        String usermail = SharedPrefFacebook.getmInstance(this).getUserInfo().get(2);
        mUserMail.setText(usermail);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //to display the fragment when selected from the
    //navigation
    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.nav_home :
                fragment = new HomeFragment();
                break;
            case R.id.nav_camera :
                fragment = new MyRequestFragment();
                break;
            case R.id.nav_gallery :
                fragment = new WriteRequestFragment();
                break;
            case R.id.nav_manage :
                fragment = new MyProfileFragment();
                break;
            case R.id.nav_slideshow :
                fragment = new MyFriendFragment();
                break;
            case R.id.nav_pendingrequest :
                fragment = new PendingRequestFragment();
                break;
            case R.id.nav_profile :
                fragment = new MyProfileFragment();
                break;
        }

        if(fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentFragment, fragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
}
