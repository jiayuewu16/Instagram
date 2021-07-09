package com.example.instagram.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.ImagesAdapter;
import com.example.instagram.PostsAdapter;
import com.example.instagram.activities.LoginActivity;
import com.example.instagram.databinding.FragmentHomeBinding;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ProfileFragment extends Fragment {

    private final static String TAG = "ProfileFragment";
    private final static int POSTS_LIMIT = 20;
    private final static int NUM_GRID_LAYOUT_COLUMNS = 3;

    private FragmentProfileBinding binding;

    private ImagesAdapter adapter;
    private List<Post> posts;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList();
        adapter = new ImagesAdapter(getActivity(), posts);

        binding.rvTimeline.setAdapter(adapter);
        binding.rvTimeline.setLayoutManager(new GridLayoutManager(getActivity(), NUM_GRID_LAYOUT_COLUMNS));

        queryPosts();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });
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
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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