package com.example.a24hnews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

import java.util.ArrayList;

import Apdater.PostApdater;
import Entiny.PostEntiny;
import Interface.AdapterListener;


import static com.example.a24hnews.MainActivity.database;

public class HistoryActivity extends AppCompatActivity {

    ImageButton imgback;
    ImageView imgDeleteAll, imgEmply;
    RecyclerView rvPost;
    ArrayList<PostEntiny> postEntinies;
    PostApdater adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();

        if (AppCompatDelegate.getDefaultNightMode()
                ==AppCompatDelegate.MODE_NIGHT_YES) {
            imgback.setImageResource(R.drawable.back_night);
            imgDeleteAll.setImageResource(R.drawable.icon_delete_night);
        }
        postEntinies = new ArrayList<>();



        rvPost.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(layoutManager);

        rvPost.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvPost.setItemAnimator(new DefaultItemAnimator());
        adapter = new PostApdater(postEntinies, new AdapterListener() {
            @Override
            public void onItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {
                PostEntiny postEntiny = (PostEntiny) o;
                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("post",postEntiny);
                startActivity(intent);

            }

            @Override
            public void onLongItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {
                PostEntiny postEntiny = (PostEntiny) o;
                DialogRemove(postEntiny.getId());

            }
        });
        GetDataHistory();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogRemoveAll();
            }
        });
        rvPost.setAdapter(adapter);
    }


    private void initView(){
        rvPost =  findViewById(R.id.rv_postss);
        imgDeleteAll =  findViewById(R.id.imgDeleteAll);
        imgEmply =  findViewById(R.id.emptyfolder);
        imgback =  findViewById(R.id.imgback);
    }
    private void GetDataHistory(){
        Cursor cursor = database.GetData("SELECT * FROM history ORDER BY id DESC");
        postEntinies.clear();
        while (cursor.moveToNext()) {
            PostEntiny postEntiny = new PostEntiny(cursor.getInt(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5),cursor.getString(6));
            postEntinies.add(postEntiny);
        }
        adapter.notifyDataSetChanged();
        if (postEntinies.size() == 0) {
            imgEmply.setVisibility(View.VISIBLE);
            imgDeleteAll.setVisibility(View.GONE);

        } else
            imgEmply.setVisibility(View.INVISIBLE);
    }

    public void DialogRemove(final int ids) {
        AlertDialog.Builder diaglogXoa = new AlertDialog.Builder(this);
        diaglogXoa.setMessage("Bạn có muốn bài viết này không?");
        diaglogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM history WHERE post_id = '" + ids + "'");
                Toast.makeText(HistoryActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                GetDataHistory();
            }
        });
        diaglogXoa.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        diaglogXoa.show();
    }

    public void DialogRemoveAll() {
        AlertDialog.Builder diaglogXoa = new AlertDialog.Builder(this);
        diaglogXoa.setMessage("Bạn có muốn xóa tất cả bài viết?");
        diaglogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM history");
                GetDataHistory();
                Toast.makeText(HistoryActivity.this, "Xóa tất cả thành công", Toast.LENGTH_SHORT).show();
            }
        });
        diaglogXoa.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        diaglogXoa.show();
    }

}
