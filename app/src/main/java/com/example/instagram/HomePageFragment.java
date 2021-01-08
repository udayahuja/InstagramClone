package com.example.instagram;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class HomePageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_page, container, false);
        mRecyclerView = view.findViewById(R.id.home_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {

        Posts post = Posts.get(getContext());
        List<SinglePost> singlePosts = post.getSinglePosts();

        if(mAdapter == null){
            mAdapter = new PostAdapter(singlePosts);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    private class PostHolder extends RecyclerView.ViewHolder{

        private SinglePost mPost;
        public PostHolder(LayoutInflater inflater, ViewGroup itemView) {
            super(inflater.inflate(R.layout.single_post, itemView, false));

        }

        public void bind(SinglePost post){
            mPost = post;
        }

    }

    private class PostAdapter extends RecyclerView.Adapter<PostHolder>{
        List<SinglePost> singlePosts;
        PostAdapter(List<SinglePost> singlePosts){
            this.singlePosts = singlePosts;
        }

        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PostHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder holder, int position) {
            SinglePost singlePost = singlePosts.get(position);
            holder.bind(singlePost);
        }

        @Override
        public int getItemCount() {
            return singlePosts.size();
        }
    }

}