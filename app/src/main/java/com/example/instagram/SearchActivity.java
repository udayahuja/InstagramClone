package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private EditText mSerachBar;
    private Button mFollowButton;
    private GetFriends mGetFriends;

    RequestQueue requestQueue;
    JsonArrayRequest request;

    List<FriendsData> mFriendsDataList;
    private static final String TAG = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.search_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Context context = this;
        mRecyclerView.addOnItemTouchListener(new RecycletTouchListener(this, mRecyclerView, new RecycletTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(context, "position "+position+" is selected",Toast.LENGTH_SHORT).show();
                Button follow_btn = view.findViewById(R.id.friend_follow_button);
                follow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"button pressed for position"+position, Toast.LENGTH_SHORT).show();
                        toggleFollowButton(follow_btn,position, context);
                    }
                });
            }
        }));

        mSerachBar = findViewById(R.id.search_searchbar);
        mFriendsDataList = new ArrayList<>();
        updateRecyclerView();

        mSerachBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAsyncronouslyAndDisplay();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void toggleFollowButton(Button follow_btn, int position, Context context) {
        FriendsData friendsData = mFriendsDataList.get(position);
        String url;
        int requestType;
        boolean isFriend = friendsData.isFriend();
        if(isFriend){
            //TODO unfollow friend
            url = MainActivity.BASE_URL+"unfollow/"+SignInFragment.user.getId()+"/"+friendsData.getId();
            requestType = Request.Method.DELETE;

        }else{
            //TODO follow the friend
            url = MainActivity.BASE_URL+"follow/"+SignInFragment.user.getId()+"/"+friendsData.getId();
            requestType = Request.Method.PUT;
        }
        Log.d(TAG, "toggleFollowButton: "+url);
        isFriend = !isFriend;
        friendsData.setFriend(isFriend);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(requestType, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        initializeFollowButton(follow_btn, isFriend);
    }


    void searchAsyncronouslyAndDisplay(){
        if(mGetFriends != null){
            request.cancel();
            mGetFriends.cancel(true);
        }
        Log.d(TAG, "searchAsyncronouslyAndDisplay: searching asynchronously");
        mGetFriends = new GetFriends(this);
        mGetFriends.execute(mSerachBar.getText().toString());
    }

    void updateRecyclerView(){
        if(mSearchAdapter==null){
            mSearchAdapter = new SearchAdapter();
            mRecyclerView.setAdapter(mSearchAdapter);
            Log.d("error", "updateRecyclerView: search recyclerview created");
        }else{
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    private class SearchHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView name;
        Button follow_btn;
        FriendsData friendsData;

        public SearchHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.search_recycler_view, parent, false));
            Log.d(TAG, "SearchHolder: holder created");
            username = itemView.findViewById(R.id.friends_username);
            name = itemView.findViewById(R.id.friends_name);
            follow_btn = itemView.findViewById(R.id.friend_follow_button);
        }

        void bind(int position){
            friendsData = mFriendsDataList.get(position);
            username.setText(friendsData.getUsername());
            name.setText(friendsData.getName());
            Log.d(TAG, "bind: "+friendsData.getName()+" is "+(friendsData.isFriend() ? "a friend" : "not a friend"));
            initializeFollowButton(follow_btn,friendsData.isFriend());
        }
    }

    void initializeFollowButton(Button follow_btn,boolean isFriend){
        String text;
        int color;
        if(isFriend){
            Log.d(TAG, "initializeFollowButton: ");
            text = "unfollow";
            color = getResources().getColor(R.color.following_btn);
        }else{
            text = "follow";
            color = getResources().getColor(R.color.follow_btn);
        }

        follow_btn.setText(text);
        follow_btn.setBackgroundColor(color);
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder>{

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new SearchHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: binding postion"+position);
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mFriendsDataList.size();
        }
    }

    private class GetFriends extends AsyncTask<String, Void, Void>{


        List<FriendsData> sFriendsData;
        Context mContext;

        GetFriends(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... strings) {

            requestQueue = Volley.newRequestQueue(mContext);
            String url = MainActivity.BASE_URL+"search/"+SignInFragment.user.getId()+"/"+strings[0];
            Log.d(TAG, "doInBackground: making request on "+url);
            request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(response == null){
                        Toast.makeText(mContext, "There are no such people",Toast.LENGTH_SHORT).show();
                    }else{
                        sFriendsData = new ArrayList<>();
                        Log.d(TAG, "onResponse: response received"+response.toString());
                        if(response == null){
                            Toast.makeText(mContext, "No such users available", Toast.LENGTH_SHORT).show();
                        }else{
                            for(int i=0;i<response.length();i++){
                                try {
                                    JSONObject jo = response.getJSONObject(i);
                                    sFriendsData.add(FriendsData.convertJsonobjectToFriendsData(jo));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        mFriendsDataList = sFriendsData;
                        updateRecyclerView();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(mContext,"there was some error please contact uday he will help you!!!", Toast.LENGTH_SHORT).show();
                }
            }
            );
            requestQueue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}