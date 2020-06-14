package Views;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.a24hnews.NoConnectActivity;
import com.example.a24hnews.R;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import API.base.NewsAPI;
import Apdater.ViewPagerAdapter;
import Entiny.CategoryEntiny;
import Util.Define;
import Util.LogUtil;
import ViewModel.Fragment_Title;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
public class HomeFragment extends Fragment {
    public static ArrayList<CategoryEntiny> listCategory;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewPager viewPager;
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager =  view.findViewById(R.id.viewPager);
        addTabs(viewPager);
        TabLayout tabLayout =  view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#3578E5"));
        listCategory = new ArrayList<>();


        return view;
    }
    private void addTabs(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        String url = Define.API_GET_CATEGORY;
        NewsAPI.getAPI(getContext(),url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                startActivity(new Intent(requireActivity(),NoConnectActivity.class));
                requireActivity().finish();

            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String s = response.body().string();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CategoryEntiny categoryEntiny = new CategoryEntiny(jsonObject);
                                adapter.add(new Fragment_Title(categoryEntiny.getCategoryId()), categoryEntiny.getCategoryName());
                                listCategory.add(categoryEntiny);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        viewPager.setAdapter(adapter);


    }
}
