package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.instagram.PostsAdapter;
import com.example.instagram.databinding.ActivityTimelineBinding;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    final static String TAG = "TimelineActivity";
    ActivityTimelineBinding binding;
    final static int POSTS_LIMIT = 20;

    PostsAdapter adapter;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(TimelineActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        posts = new ArrayList();
        adapter = new PostsAdapter(this, posts);

        binding.rvTimeline.setAdapter(adapter);
        binding.rvTimeline.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refreshPosts();
            }
        });

        /*Intent intent = new Intent(TimelineActivity.this, CreateActivity.class);
        startActivity(intent);*/
    }

    private void refreshPosts() {
        posts.clear();
        queryPosts();
        binding.swipeContainer.setRefreshing(false);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POSTS_LIMIT);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                else {
                    for (Post post : queryPosts) {
                        Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                    }
                    posts.addAll(queryPosts);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}