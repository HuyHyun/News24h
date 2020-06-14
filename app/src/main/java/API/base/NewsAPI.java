package API.base;

import android.content.Context;
import android.widget.Toast;

import Util.Define;
import Util.LogUtil;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class NewsAPI {

    public static void getListPost(Context context,int categoryId, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = Define.API_GET_LIST_POST + "?category_id=" + categoryId;
        Request request = new Request.Builder()
                .url(url).build();

        LogUtil.d("tenlink",url);
        client.newCall(request).enqueue(callback);
    }

    public static void getAPI(Context context,String url,Callback callback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }


    public static void getPostDetail(Context context,int postId,Callback callback){
        OkHttpClient client = new OkHttpClient();
        String url = Define.API_GET_POST_DETAIL + "?post_id=" + postId;
        Request request = new Request.Builder()
                .url(url).build();

        client.newCall(request).enqueue(callback);
    }




}
