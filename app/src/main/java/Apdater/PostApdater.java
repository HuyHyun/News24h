package Apdater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a24hnews.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Entiny.PostEntiny;
import Interface.AdapterListener;

public class PostApdater extends RecyclerView.Adapter {

    private ArrayList<PostEntiny> postEntinies;
    private ArrayList<PostEntiny> arrayListPost;
    private int format = 1; //1 phút 2 giờ 3 ngày
    private AdapterListener listener;


    public PostApdater(ArrayList<PostEntiny> postEntinies, AdapterListener listener) {
        this.postEntinies = postEntinies;
        this.listener = listener;
        this.arrayListPost = new ArrayList<>();
        this.arrayListPost.addAll(postEntinies);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, null);
        PostViewHolder postViewHolder = new PostViewHolder(view);
        return postViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final PostEntiny postEntiny = postEntinies.get(position);
        final PostViewHolder postViewHolder = (PostViewHolder) holder;
        postViewHolder.tvTitle.setText(postEntiny.getTitle());


        int time = FormatTime(postEntiny.getDate());
        if (format == 1) {
            postViewHolder.tvDateTime.setText(time + " phút");
        } else if (format == 2) {
            postViewHolder.tvDateTime.setText(time + " giờ");
        } else
            postViewHolder.tvDateTime.setText(time + " ngày");

        postViewHolder.tvSource.setText(postEntiny.getSource());


        Glide.with(postViewHolder.imgThumb.getContext()).load(postEntiny.getThumb()).into(postViewHolder.imgThumb);

        postViewHolder.rlPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickListener(postEntiny, position, postViewHolder);
                }
            }
        });

        postViewHolder.rlPost.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongItemClickListener(postEntiny, position, postViewHolder);

                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return postEntinies.size();
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        postEntinies.clear();
        if (charText.length() == 0) {
            postEntinies.addAll(arrayListPost);
        } else {
            for (PostEntiny postEntiny : arrayListPost) {
                if (postEntiny.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    postEntinies.add(postEntiny);
                }
            }
        }
        notifyDataSetChanged();
    }


    private int FormatTime(String date) {

        int result = 0;
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormatter.setLenient(false);
        Date currentTime, startDate;
        String curDate = dateFormatter.format(Calendar.getInstance().getTime());
        try {
            currentTime = dateFormatter.parse(curDate);
            startDate = dateFormatter.parse(date);
            long endValue = startDate.getTime();
            long startValue = currentTime.getTime();
            long temp = startValue - endValue;
            result = (int) (temp / (60 * 1000));

            if (result > 60) {
                format = 2;
                result /= 60;
                if (result > 25 && result < 24 * 30) {
                    result /= 24;
                    format = 3;
                }

            } else
                format = 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvTitle, tvSource, tvDateTime;
        RelativeLayout rlPost;


        private PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_thumb);
            tvTitle = itemView.findViewById(R.id.tv_title);
            rlPost = itemView.findViewById(R.id.rl_post);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvSource = itemView.findViewById(R.id.tv_source);
        }
    }

}









