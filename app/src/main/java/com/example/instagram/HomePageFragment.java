package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private ImageView mSearchFriends;
    private List<SinglePost> singlePosts;

    private static final String TAG = "error";

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
        mSearchFriends = view.findViewById(R.id.home_serach_view);
        //updateUI();
        if(singlePosts==null){
            singlePosts = new ArrayList<>();
        }
        initializePosts();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });



    }

    private void initializePosts(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = MainActivity.BASE_URL+"get-feeds/"+SignInFragment.user.getId();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                singlePosts.clear();
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject jo = response.getJSONObject(i);
                        SinglePost s = SinglePost.jsontoJava(jo);
                        //Log.d(TAG, "Printing data that is received "+s.getName() +" "+s.getUsername()+" "+s.getNoOfLikes() + " "+ s.getNoOfComments());
                        singlePosts.add(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: ");
            }
        });
        queue.add(request);
    }

    private void updateUI() {

        Log.d("error", "updateUI: UI updated");

        if(mAdapter == null){
            mAdapter = new PostAdapter(singlePosts);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    private class PostHolder extends RecyclerView.ViewHolder{

        private SinglePost mPost;
        private TextView name;
        private TextView noOfLikes;
        private TextView caption;
        public PostHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.single_post, parent, false));
            noOfLikes = (TextView) itemView.findViewById(R.id.single_post_likes);
            caption = itemView.findViewById(R.id.single_post_caption);
            name = itemView.findViewById(R.id.single_post_name);
            if(caption == null || noOfLikes == null)
                Log.d("error", "PostHolder: not initialized");
        }

        public void bind(SinglePost post){
            mPost = post;
            //Log.d("error", "bind: "+post.getNoOfLikes()+" no. of likes!!!");
            caption.setText(post.getCaption());
            noOfLikes.setText(post.getNoOfLikes()+" no. of likes!!!");
            name.setText(post.getName());

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