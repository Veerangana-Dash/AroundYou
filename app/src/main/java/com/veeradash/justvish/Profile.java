package com.veeradash.justvish;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mapsxml);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mToggle =new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.Open,R.string.Close);
        //noinspection deprecation
        mDrawerLayout.setDrawerListener(mToggle);


        mNavigationView = (NavigationView) findViewById(R.id.nav);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:

                        Intent i_h = new Intent(Profile.this, MapsActivity1.class);
                        startActivity(i_h);
                        return true;
                    case R.id.profile:
                        Intent i_p = new Intent(Profile.this, Profile.class);
                        startActivity(i_p);
                        break;
                    case R.id.newsfeed:
                        Intent i_n = new Intent(Profile.this, Newsfeed.class);
                        startActivity(i_n);
                        return true;
                    case R.id.events:
                        Intent i_e = new Intent(Profile.this, Events.class);
                        startActivity(i_e);
                        return true;
                    case R.id.wallet:
                        Intent i_w = new Intent(Profile.this, Wallet.class);
                        startActivity(i_w);
                        return true;
                    case R.id.setting:
                        Intent i_s = new Intent(Profile.this, Settings.class);
                        startActivity(i_s);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(Profile.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                }
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profilemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, Editprofile.class));
                return true;
            case R.id.delete:
                startActivity(new Intent(this, Deleteprofile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
