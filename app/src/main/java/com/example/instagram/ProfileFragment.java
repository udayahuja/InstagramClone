package com.example.instagram;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ProfileFragment extends Fragment {


    private GridView mGridView;
    private TextView mNoOfPosts;
    private TextView mZeroPosts;
    private TextView mNoOfFollowers;
    private TextView mNoOfFollowing;
    private TextView mUsername;
    private TextView mName;
    private ImageAdapter adapter;

    private final String TAG = "error";

    private int tempInt = 0;
    private Button tempIncrement;
    private Button tempDecrement;

    public Integer[] mTumbIds = {R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index,
            R.drawable.account_photo, R.drawable.blacklogo, R.drawable.images, R.drawable.index
    };

    //public Integer []mTumbIds = {};

    public ProfileFragment() {
        // Required empty public constructor
    }

    /*
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemSelected = item.getItemId();
        if(itemSelected == R.id.settings){
            Toast.makeText(getContext(),"settings selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mGridView = view.findViewById(R.id.profile_grid_view);
        adapter= new ImageAdapter(getContext());
        mGridView.setAdapter(adapter);
        mNoOfPosts = view.findViewById(R.id.profile_no_of_posts);
        mZeroPosts = view.findViewById(R.id.profile_zero_posts);
        mNoOfFollowers = view.findViewById(R.id.profile_no_of_followers);
        mNoOfFollowing = view.findViewById(R.id.profile_no_of_following);
        mUsername = view.findViewById(R.id.profile_username);
        mName = view.findViewById(R.id.profile_name);

        tempIncrement = view.findViewById(R.id.increment_button);
        tempDecrement = view.findViewById(R.id.decrement_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoOfPosts.setText(tempInt+"\nPosts");
        mUsername.setText(SignInFragment.user.getUsername());
        mName.setText(SignInFragment.user.getName());
        initializeFollowersAndFollowing(getContext());
        tempIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempInt<mTumbIds.length) {
                    tempInt += 1;
                    updateGrid();
                }
            }
        });
        tempDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempInt-=1;
                if(tempInt<0) {
                    tempInt = 0;
                }else{
                    updateGrid();
                }
            }
        });
    }

    private void initializeFollowersAndFollowing(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = MainActivity.BASE_URL + "no-of-followers-and-following/"+SignInFragment.user.getId();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response == null){
                    Toast.makeText(context, "There was some error while getting followers",Toast.LENGTH_SHORT).show();
                }
                String []s = response.split("-");
                mNoOfFollowers.setText(s[0]+"\nFollowers");
                mNoOfFollowing.setText(s[1]+"\nFollowing");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: error while getting followers and following");
            }
        });
        queue.add(request);
    }

    void updateGrid(){
        mNoOfPosts.setText(tempInt+"\nPosts");
        if(tempInt == 0){
            mZeroPosts.setVisibility(View.VISIBLE);
        }else{
            mZeroPosts.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }


    class ImageAdapter extends BaseAdapter{

        private Context mContext;

        ImageAdapter(Context context){
            mContext = context;
        }
        @Override
        public int getCount() {
            return tempInt;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(250,250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 4, 0, 4);
            }else{
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mTumbIds[position]);
            return imageView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeFollowersAndFollowing(getContext());
    }
}