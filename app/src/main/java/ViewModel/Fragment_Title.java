package ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.a24hnews.DetailActivity;
import com.example.a24hnews.MainActivity;
import com.example.a24hnews.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import API.base.NewsAPI;
import Apdater.PostApdater;
import Entiny.PostEntiny;
import Interface.AdapterListener;
import Util.LogUtil;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_Title extends Fragment {
    private int categoryId;
    private RecyclerView rvPost;
    private ArrayList<PostEntiny> postEntinies = new ArrayList();
    private PostApdater adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Fragment_Title() {
    }

    public Fragment_Title(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        rvPost = view.findViewById(R.id.rv_post);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postEntinies.clear();
                        adapter.notifyDataSetChanged();
                        getListPost();
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 3000); // Delay in millis
            }
        });
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        rvPost.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(layoutManager);
        rvPost.setItemAnimator(new DefaultItemAnimator());
        rvPost.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        adapter = new PostApdater(postEntinies, new AdapterListener() {
            @Override
            public void onItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {
                PostEntiny postEntiny = (PostEntiny) o;
                try{
                    MainActivity.database.QueryData("DELETE FROM post_id WHERE history = '" + postEntiny.getId() + "'");
                }catch(Exception e) {e.printStackTrace();}

                MainActivity.database.INSERT_Data("history", postEntiny);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("post",postEntiny);
                startActivity(intent);

            }

            @Override
            public void onLongItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder) {

            }
        });
        rvPost.setAdapter(adapter);

        postEntinies.clear();
        getListPost();

        return view;

    }

    private void getListPost(){
        NewsAPI.getListPost(getContext(), categoryId, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                swipeRefreshLayout.setRefreshing(false);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String s = response.body().string();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONArray jsonArray = new JSONArray(s);

                            for (int i =0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PostEntiny postEntiny = new PostEntiny(jsonObject);
                                postEntinies.add(postEntiny);
                                LogUtil.d("postEntiny", postEntiny.toString());
                            }
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){

                            LogUtil.d("postEntiny","khong load duoc");
                            e.printStackTrace();

                        }
                    }
                });

            }
        });
    }
}
