package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.activities.DetailsActivity;
import com.example.instagram.databinding.ItemPostBinding;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public final static String POST_KEY = "post_key";

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(context));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ItemPostBinding binding;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            binding.tvDescription.setText(post.getDescription());
            binding.tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(binding.ivPhoto);
            }
            else {
                binding.ivPhoto.setImageDrawable(null);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            Post post = posts.get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
            context.startActivity(intent);
        }
    }
}
