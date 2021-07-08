package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.ActivityDetailsBinding;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        binding.tvUsername.setText(post.getUser().getUsername());
        binding.tvDescription.setText(post.getDescription());
        binding.tvDate.setText(post.getTime());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(binding.ivPhoto);
        }
        else {
            binding.ivPhoto.setImageDrawable(null);
        }
    }
}