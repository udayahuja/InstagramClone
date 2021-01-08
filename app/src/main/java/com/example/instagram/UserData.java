package com.example.instagram;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    private int id;
    private String name;
    private String username;
    private String email;
    private Long phoneNo;
    private String DOB;
    private String profilePicUrl;
    private String bio;
    private String password;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public JSONObject toJson(){

        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("username", username);
        param.put("password", password);
        param.put("email", email);
        param.put("phoneNo", phoneNo.toString());
        param.put("dob", DOB);
        param.put("profilephotoURL", profilePicUrl);
        param.put("bio", bio);
        return new JSONObject(param);
    }
    public JSONObject toJson(int a){

        JSONObject param = new JSONObject();
        //Map<String, String> param = new HashMap<>();

        try {
            param.put("name", name);
            param.put("username", username);
            param.put("password", password);
            param.put("email", email);
            if(phoneNo!=null)
            param.put("phoneNo", phoneNo.toString());
            param.put("DOB", DOB);
            param.put("profilephotoUrl", profilePicUrl);
            param.put("bio", bio);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param;
    }

    public Map<String, String> map(){
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("username", username);
        param.put("password", password);
        param.put("email", email);
        param.put("phoneNo", phoneNo.toString());
        param.put("DOB", DOB);
        param.put("profilephotoUrl", profilePicUrl);
        param.put("bio", bio);
        return param;
    }
}
