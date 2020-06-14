package com.example.a24hnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import API.base.NewsAPI;
import Apdater.CommentAdapter;
import Entiny.CategoryEntiny;
import Entiny.PostEntiny;
import Entiny.UserEntiny;
import Util.LogUtil;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static Views.HomeFragment.listCategory;
import static com.example.a24hnews.MainActivity.database;
import static com.example.a24hnews.R.drawable.time;

public class DetailActivity extends AppCompatActivity {

    private ProgressBar pgLoad;
    private ShareDialog shareDialog;
    private ImageButton imgShare, imgLuu, imgBack, imgSend;
    private ShareLinkContent shareLinkContent;
    private WebView webView;
    private TextView tvTitle, tvSource, tvLink,tvCategory;
    private EditText editComment;
    private String link = "", checkName, checkid, data;
    private DatabaseReference mData;
    private RecyclerView rvComment;
    private CommentAdapter adapter;
    private ArrayList<UserEntiny> userEntinies;
    private boolean isLoading = false;
    private PostEntiny postEntiny;
    private long maxid = 0;
    private List<UserEntiny> temp;
    private long dem;
    SharedPreferences sharedPreferences;
    private Boolean flag,checkLoadMore , checkEmpty, checkLogin, checkNight = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();


        final WebSettings webSettings = webView.getSettings();
        shareDialog = new ShareDialog(this);
        webSettings.setDefaultFontSize(20);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            checkNight = true;
            editComment.setBackgroundResource(R.drawable.cutsom_edit_text_night);
//            webSettings.setForceDark(WebSettings.FORCE_DARK_ON);

        }

        Bundle bundle = getIntent().getExtras();
        postEntiny = (PostEntiny) bundle.getSerializable("post");
        tvTitle.setText(postEntiny.getTitle());
        for (CategoryEntiny categoryEntiny: listCategory) {
            if(categoryEntiny.getCategoryId()==postEntiny.getCategory()){
                tvCategory.setText(categoryEntiny.getCategoryName());
                break;
            }
        }
        tvSource.setText(postEntiny.getSource());
        NewsAPI.getPostDetail(this, postEntiny.getId(), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                pgLoad.setVisibility(View.INVISIBLE);
                startActivity(new Intent(DetailActivity.this, NoConnectActivity.class));

            }

            @Override
            public void onResponse(Response response) throws IOException {

                final String s = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            checkEmpty = true;
                            JSONObject jsonObject = new JSONObject(s);
                            PostEntiny post = new PostEntiny(jsonObject);


                            link = post.getLink();
                            if (checkNight) {
                                data = "<html><head><title></title><style>*{max-width:100%; background-color: 302f2f;color: dddddc;}</style></head><body>" + post.getContent() + "</body></html>";
                            } else {
                                data = "<html><head><title></title><style>*{max-width:100%;}</style></head><body>" + post.getContent() + "</body></html>";
                            }
                            webView.loadData(data, "text/html; charset=utf-8", "utf-8");

                            LogUtil.d("noidung",post.getContent());


                        } catch (Exception e) {
                            checkEmpty = false;
                            if (checkNight) {
                                data = "<html><head><title></title><style>*{max-width:100%; background-color: 302f2f;color: dddddc;font-size: 150%;}</style></head><body> Bài viết không tồn tại hoặc đã bị xóa</body></html>";
                            } else {
                                data = "<html><head><title></title><style>*{max-width:100%;font-size: 150%;}</style></head><body> Bài viết không tồn tại hoặc đã bị xóa</body></html>";
                            }
                            webView.loadData(data, "text/html; charset=utf-8", "utf-8");
                            pgLoad.setVisibility(View.GONE);
                            e.printStackTrace();
                            editComment.setVisibility(View.GONE);
                            imgSend.setVisibility(View.GONE);
                            imgLuu.setVisibility(View.GONE);
                            imgShare.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView.getProgress() == 100) {
                    pgLoad.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient());

        if (checkDanhDau(postEntiny.getId()) == 1) {
            imgLuu.setImageResource(R.drawable.bookmark_light);
            flag = true;
        } else {
            imgLuu.setImageResource(R.drawable.bookmark);
            flag = false;
        }
        imgLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    flag = true;
                    imgLuu.setImageResource(R.drawable.bookmark_light);
                    checkDanhDau(postEntiny.getId());

                    MainActivity.database.INSERT_Data("bookmarks", postEntiny);
                    if (BookmarksActivity.adapter != null) {
                        BookmarksActivity.GetDataBookmark();
                    }

                } else {
                    flag = false;
                    imgLuu.setImageResource(R.drawable.bookmark);
                    database.QueryData("DELETE FROM bookmarks WHERE post_id = '" + postEntiny.getId() + "'");
                    if (BookmarksActivity.adapter != null) {
                        BookmarksActivity.GetDataBookmark();
                    }

                }
            }
        });


        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty) {
                    if (checkLogin) {
                        if (shareDialog.canShow(ShareLinkContent.class)) {
                            shareLinkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(link)).build();
                        }
                        shareDialog.show(shareLinkContent);
                    } else
                        Toast.makeText(DetailActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mData = FirebaseDatabase.getInstance().getReference().child("User" + postEntiny.getId());


        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());
                    dem = maxid;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkEmpty) {
                    if (maxid < 4) {
                        checkLoadMore = false;
                        mData.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                UserEntiny userEntiny = dataSnapshot.getValue(UserEntiny.class);
                                userEntinies.add(0, userEntiny);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        loadFromFirebase(2, maxid - 1, maxid);
                        checkLoadMore = true;
                    }

                    LogUtil.d("maxid", maxid + "");
                }
            }
        }, 4000);


        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!link.equals("")) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(in);
                }

            }
        });


        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editComment.getText().toString();
                if (checkLogin && !comment.equals("")) {
                    LogUtil.d("checklogin", checkLogin.toString());
                    UserEntiny u = new UserEntiny(maxid + 1, checkName, comment, checkid);
                    mData.push().setValue(u);
                    Toast.makeText(DetailActivity.this, "comment thành công", Toast.LENGTH_SHORT).show();

                    editComment.setText("");

                } else if (comment.equals("")) {
                    Toast.makeText(DetailActivity.this, "Bạn chưa nhập comment", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DetailActivity.this, "comment thất bại", Toast.LENGTH_SHORT).show();
            }
        });


        adapter = new CommentAdapter(userEntinies, this);
        initScrollListener();
        rvComment.setAdapter(adapter);

    }

    public int checkDanhDau(int postId) {
        Cursor cursor = database.GetData("SELECT COUNT(*) FROM bookmarks WHERE post_id = '" + postId + "'");
        return (cursor.moveToFirst()) ? cursor.getInt(0) : 0;

    }

    private void init() {
        rvComment = findViewById(R.id.rv_comment);
        rvComment.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvComment.setLayoutManager(layoutManager);
        userEntinies = new ArrayList<>();
        imgShare = findViewById(R.id.imgShare);
        pgLoad = findViewById(R.id.webload);
        pgLoad.setVisibility(View.VISIBLE);
        imgLuu = findViewById(R.id.imgSave);
        imgBack = findViewById(R.id.imgBack);
        webView = findViewById(R.id.web);
        tvTitle = findViewById(R.id.title_post);
        editComment = findViewById(R.id.edit_comment);
        imgSend = findViewById(R.id.imgSend);
        temp = new ArrayList<>();
        tvSource = findViewById(R.id.tv_source);
        sharedPreferences = this.getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
        checkName = sharedPreferences.getString("fullname", "fail");
        checkid = sharedPreferences.getString("id", "fail");
        checkLogin = sharedPreferences.getBoolean("checklogin", false);
        tvLink = findViewById(R.id.tv_link);
        tvCategory = findViewById(R.id.tvCategory);

    }


    private void initScrollListener() {
        rvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userEntinies.size() - 1 && checkLoadMore) {
                        isLoading = true;
                        loadMore();


                    }
                }

            }
        });
    }

    private void loadMore() {

        userEntinies.add(null);
        adapter.notifyItemInserted(userEntinies.size() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userEntinies.remove(userEntinies.size() - 1);
                int scrollPosition = userEntinies.size();
                adapter.notifyItemRemoved(scrollPosition);

                dem = dem - 2;
                long nextLimit = dem - 1;
                loadFromFirebase((int) maxid, nextLimit, dem);

            }

        }, 2000); // Delay in millis


    }

    public void loadFromFirebase(int limit, long startValue, long endValue) {

        temp.clear();
        mData.limitToLast(limit).orderByChild("id").startAt(startValue).endAt(endValue).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                UserEntiny userEntiny = dataSnapshot.getValue(UserEntiny.class);
                temp.add(0, userEntiny);

                if (temp.size() == 2) {
                    userEntinies.addAll(temp);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
