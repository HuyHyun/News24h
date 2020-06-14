package Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface AdapterListener {

     void onItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder);

     void onLongItemClickListener(Object o, int pos, RecyclerView.ViewHolder holder);
}
