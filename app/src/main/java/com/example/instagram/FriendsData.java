package com.example.instagram;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendsData {

    private int id;
    private String username;
    private String name;
    private String profilepic;
    private boolean isFriend;

    FriendsData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public static FriendsData convertJsonobjectToFriendsData(JSONObject jo){
        FriendsData f = new FriendsData();
        try {
            f.setName(jo.getString("name"));
            f.setId(jo.getInt("id"));
            f.setUsername(jo.getString("username"));
            f.setProfilepic(jo.getString("profilephotourl"));
            f.setFriend(jo.getInt("friend") == 1);
        } catch (JSONException e) {
            Log.d("error", "convertJsonobjectToFriendsData: error while parsing the data");
            e.printStackTrace();
        }
        return f;
    }
}
