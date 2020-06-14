package com.example.a24hnews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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

public class BookmarksActivity extends AppCompatActivity {
    ImageButton imgback;
    static ImageView imgDeleteAll;
    static ImageView imgEmply;
    RecyclerView rvPost;
    static ArrayList<PostEntiny> postEntinies;
    static PostApdater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        initView();
        postEntinies = new ArrayList<>();

        rvPost.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(layoutManager);
        rvPost.setItemAnimator(new DefaultItemAnimator());
        rvPost.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvPost.setItemAnimator(new DefaultItemAnimator());
        adapter = new PostApdater(postEntinies, new AdapterListener() {
            @Override
            public void onItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {
                PostEntiny postEntiny = (PostEntiny) o;
                Intent intent = new Intent(BookmarksActivity.this, DetailActivity.class);
                intent.putExtra("post",postEntiny);
                startActivity(intent);
            }
            @Override
            public void onLongItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {
                PostEntiny postEntiny = (PostEntiny) o;
                DialogRemove(postEntiny.getId());
            }
        });
        GetDataBookmark();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvPost.setAdapter(adapter);
        imgDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiglogRemoveAll();
            }
        });
    }


    private void initView(){
        rvPost =  findViewById(R.id.rv_post);
        imgDeleteAll =  findViewById(R.id.imgDeleteAll);
        imgEmply =  findViewById(R.id.emptyfolder);
        imgback =  findViewById(R.id.imgback);
    }

    public static void GetDataBookmark() {
        Cursor cursor = database.GetData("SELECT * FROM bookmarks ORDER BY id DESC");
        if (postEntinies != null) {
            postEntinies.clear();
        }
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
        AlertDialog.Builder dilogRemove = new AlertDialog.Builder(this);
        dilogRemove.setMessage("Bạn có muốn bài viết này không?");
        dilogRemove.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM bookmarks WHERE post_id = '" + ids + "'");
                Toast.makeText(BookmarksActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                GetDataBookmark();
            }
        });
        dilogRemove.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dilogRemove.show();
    }


    public void DiglogRemoveAll() {
        AlertDialog.Builder diaglogXoa = new AlertDialog.Builder(this);
        diaglogXoa.setMessage("Bạn có muốn xóa tất cả bài viết?");
        diaglogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM bookmarks");
                GetDataBookmark();
                Toast.makeText(BookmarksActivity.this, "Xóa tất cả thành công   ", Toast.LENGTH_SHORT).show();
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
