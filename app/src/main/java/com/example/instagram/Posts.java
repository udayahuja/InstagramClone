package com.example.instagram;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Posts {

    private static Posts sPosts;
    private List<SinglePost> mSinglePosts;

    private Posts(Context context){
        mSinglePosts = new ArrayList<>();

        Random r = new Random();
        for(int i=0;i<50;i++){
            SinglePost s = new SinglePost();
            s.setName("Post No:"+(i+1));
            s.setNoOfLikes(r.nextInt(1000));
            s.setNoOfComments(r.nextInt(1000));
            mSinglePosts.add(s);
        }
    }

    public SinglePost getPost(int i){
        return mSinglePosts.get(i);
    }

    public List<SinglePost> getSinglePosts(){
        return mSinglePosts;
    }

    public static Posts get(Context context){
        if(sPosts == null){
            sPosts = new Posts(context);
        }
        return sPosts;
    }

}
