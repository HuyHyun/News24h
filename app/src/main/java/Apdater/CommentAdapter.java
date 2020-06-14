package Apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a24hnews.R;
import com.facebook.login.widget.ProfilePictureView;
import java.util.ArrayList;
import Entiny.UserEntiny;


public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<UserEntiny> userEntinies;
    private Context context;

    public CommentAdapter(ArrayList<UserEntiny> userEntinies, Context context) {
        this.userEntinies = userEntinies;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String name = userEntinies.get(position).getName();
        viewHolder.tvUserName.setText(name);
        String cmt = userEntinies.get(position).getComment();
        viewHolder.tvComment.setText(cmt);
        String profileId = userEntinies.get(position).getProfileId();
        viewHolder.profilePictureView.setProfileId(profileId);

    }





    @Override
    public int getItemCount() {
        return userEntinies == null ? 0 : userEntinies.size();
    }
    @Override
    public int getItemViewType(int position) {
        return userEntinies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder{

        ProfilePictureView profilePictureView;
        TextView tvUserName,tvComment;
        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureView =  itemView.findViewById(R.id.friendProfilePicture);
            tvUserName =  itemView.findViewById(R.id.tv_user_name);
            tvComment =  itemView.findViewById(R.id.tv_comment);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        private LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }




    }





}
