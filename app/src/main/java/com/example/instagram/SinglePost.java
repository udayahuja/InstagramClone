package com.example.instagram;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

public class SinglePost {

    private int picid;
    private int id;
    private byte[] postUrl;
    private String caption;
    private String username;
    private int noOfLikes;
    private int noOfComments;

    private Timestamp postTime;

    public SinglePost() {
    }

    public int getPicid() {
        return picid;
    }

    public void setPicid(int picid) {
        this.picid = picid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(byte[] postUrl) {
        this.postUrl = postUrl;
    }

    public int getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(int noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public int getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(int noOfComments) {
        this.noOfComments = noOfComments;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    static SinglePost jsonToJava(JSONObject jo){
        SinglePost s = new SinglePost();
        try {

            Log.d("error", "jsonToJava: "+jo+" "+jo.getInt("nooflikes")+" "+jo.getInt("noofcomments")+" " +jo.getString("caption"));
            s.setPicid(jo.getInt("picid"));
            s.setId(jo.getInt("id"));
            //s.setPostUrl();

            Object obj = jo.get("url");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            s.setPostUrl(out.toByteArray());

            s.setCaption(jo.getString("caption"));
            s.setUsername(jo.getString("username"));
            s.setNoOfLikes(jo.getInt("nooflikes"));
            s.setNoOfComments(jo.getInt("noofcomments"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }

    static SinglePost jsontoJava(JSONObject jsonObject){
        SinglePost s = new SinglePost();

        try {
            Log.d("error", "jsonToJava: "+jsonObject.getInt("nooflikes")+" "+jsonObject.getInt("noofcomments")+" " +jsonObject.getString("caption"));
            s.setNoOfLikes(jsonObject.getInt("nooflikes"));
            s.setUsername(jsonObject.getString("username"));
            s.setName(jsonObject.getString("username"));
            s.setCaption(jsonObject.getString("caption"));
            s.setNoOfComments(jsonObject.getInt("noofcomments"));
            s.setId(jsonObject.getInt("id"));
            s.setPicid(jsonObject.getInt("picid"));

            Object obj = jsonObject.get("url");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            s.setPostUrl(out.toByteArray());

        } catch (JSONException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }
}

/*
*   TODO deserialize the byte array
*
*
* public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    return is.readObject();
  }
*
* */