package com.bilalkadam.bitirmeproje;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {
    private ArrayList<String> commentList;
    private ArrayList<String> commentNameList;
    private ArrayList<String> commentImageList;

    public CommentRecyclerAdapter(ArrayList<String> commentList, ArrayList<String> commentNameList, ArrayList<String> commentImageList) {
        this.commentList = commentList;
        this.commentNameList = commentNameList;
        this.commentImageList = commentImageList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row_comment,parent,false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.commentText.setText(commentList.get(position));
        holder.commentNameText.setText(commentNameList.get(position));
        Picasso.get().load(commentImageList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        TextView commentText;
        TextView commentNameText;
        ImageView imageView;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.recyclerView_row_comment);
            imageView = itemView.findViewById(R.id.recyclerView_row_comment_image);
            commentNameText = itemView.findViewById(R.id.recyclerView_row_comment_name);
        }
    }
}
