package com.techhaven.schooltweets.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techhaven.schooltweets.R;
import com.techhaven.schooltweets.dataaccesslayer.contracts.DataContract;
import com.techhaven.schooltweets.dataaccesslayer.provider.DataProvider;
import com.techhaven.schooltweets.fragment.ForumFragment;
import com.techhaven.schooltweets.networks.VolleySingleton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ForumFragment.OnFragmentInteractionListener {
    LinearLayout container;
    DrawerLayout drawer;
    TextView txtNavName, txtNavEmail, txtNavLogin;
    Boolean exitApp = false;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Navigation drawer header views
        txtNavEmail = (TextView) navigationView.findViewById(R.id.nav_header_email);
        txtNavLogin = (TextView) navigationView.findViewById(R.id.nav_header_login);
        txtNavName = (TextView) navigationView.findViewById(R.id.nav_header_name);
        setUpNavHeader();
        testContentProvider();

        container = (LinearLayout) findViewById(R.id.layoutContainer);
        fragment = ForumFragment.newInstance("Forum", "");
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutContainer, fragment)
                .commit();

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, "http://php.net/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        //requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private void setUpNavHeader() {
        txtNavEmail.setVisibility(View.GONE);
        txtNavName.setVisibility(View.GONE);
        txtNavLogin.setVisibility(View.VISIBLE);
        txtNavLogin.setOnClickListener(loginPage);
    }

    View.OnClickListener loginPage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exitApp) {
            super.onBackPressed();

        } else {
            Snackbar.make(container, R.string.app_exit_on_back_button_text, Snackbar.LENGTH_SHORT).setAction("Exit", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            }).show();
            //Toast.makeText(MainActivity.this, R.string.app_exit_on_back_button_text, Toast.LENGTH_SHORT).show();
            exitApp = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        exitApp = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private void testContentProvider() {
        ContentValues values = new ContentValues();
        values.put(DataContract.NewsEntry.COLUMN_AUTHOR_NAME, "Yomi");
        values.put(DataContract.NewsEntry.COLUMN_CONTENT, "Story story");
        values.put(DataContract.NewsEntry.COLUMN_TITLE, "New Story");
        values.put(DataContract.NewsEntry.COLUMN_DATE_POSTED, "20160222");
        DataProvider dataProvider = new DataProvider();
//        Uri uri=getContentResolver().insert(DataContract.NewsEntry.CONTENT_URI, values);
//        Toast.makeText(MainActivity.this,uri.toString(),Toast.LENGTH_SHORT).show();
//        Cursor cursor=getContentResolver().query(DataContract.NewsEntry.CONTENT_URI, null, null, null, null);
//        Toast.makeText(MainActivity.this,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
//        cursor.moveToFirst();
//        // uri.getQuery()
//        cursor.close();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_newblog_feed) {
            menuItem.setChecked(true);
            setTitle(getString(R.string.action_title_news));
            fragment = ForumFragment.newInstance("News Blog feed", "");

        } else if (id == R.id.nav_forum) {
            menuItem.setChecked(true);
            setTitle(getString(R.string.action_title_forum));
            fragment = ForumFragment.newInstance("Forum", "");
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutContainer, fragment)
                .commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
