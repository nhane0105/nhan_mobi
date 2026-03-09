package com.example.baitap4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList = new ArrayList<>();

    public void setPostList(List<Post> posts) {
        postList.clear();
        if (posts != null) {
            postList.addAll(posts);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textPostId.setText("ID: " + post.getId() + " | UserId: " + post.getUserId());
        holder.textPostTitle.setText(post.getTitle());
        holder.textPostBody.setText(post.getBody());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textPostId;
        TextView textPostTitle;
        TextView textPostBody;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostId = itemView.findViewById(R.id.textPostId);
            textPostTitle = itemView.findViewById(R.id.textPostTitle);
            textPostBody = itemView.findViewById(R.id.textPostBody);
        }
    }
}
