package com.example.a24hnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import SQLiteDatabase.DatabaseSQLi;
import Util.LogUtil;
import Views.HomeFragment;
import Views.SearchFragment;
import Views.UserFragment;

import static Views.UserFragment.checkNight;
import static Views.UserFragment.shareNight;


public class MainActivity extends AppCompatActivity {

    public static DatabaseSQLi database;
    private Fragment home = new HomeFragment();
    private Fragment search = new SearchFragment();
    private Fragment user = new UserFragment();
    private Fragment active = home;
    private FragmentManager fm =getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shareNight = this.getSharedPreferences("dataNight", Context.MODE_PRIVATE);
        checkNight = shareNight.getBoolean("night",false);

        if(checkNight) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        CreateTable();
        BottomNavigationView bottomNavigationView =  findViewById(R.id.botom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        fm.beginTransaction().add(R.id.frame_nav, user, "3").hide(user).commit();
        fm.beginTransaction().add(R.id.frame_nav, search, "2").hide(search).commit();
        fm.beginTransaction().add(R.id.frame_nav, home, "1").commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            fm.beginTransaction().hide(active).show(home).commit();
                            active = home;
                            return true;
                        case R.id.nav_search:
                            fm.beginTransaction().hide(active).show(search).commit();
                            active = search;
                            return true;
                        case R.id.nav_user:
                            fm.beginTransaction().hide(active).show(user).commit();
                            active = user;
                            return true;
                    }
                    return false;
                }
            };

    public void CreateTable(){
        database = new DatabaseSQLi(this, "Manager.sqlite", null, 2);
        database.QueryData("CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY AUTOINCREMENT, post_id INT UNIQUE,post_title TEXT, post_desc TEXT, post_thumb TEXT,category_id INT, date_time DATETIME)");
        database.QueryData("CREATE TABLE IF NOT EXISTS bookmarks(id INTEGER PRIMARY KEY AUTOINCREMENT, post_id INT UNIQUE,post_title TEXT, post_desc TEXT, post_thumb TEXT,category_id INT, date_time DATETIME)");
    }


}
