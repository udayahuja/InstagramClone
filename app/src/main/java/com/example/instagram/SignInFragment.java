package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignInFragment extends Fragment {

    private TextView mDontHaveAnAcc;
    private FrameLayout mFrameLayout;
    private EditText mLoginEmail;
    private EditText mLoginPass;
    private Button mLoginButton;

    private Button trial;

    private static final String TAG = "error";
    static UserData user;
    //static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SignInFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        mDontHaveAnAcc = view.findViewById(R.id.dontHaveAnAccount);
        mFrameLayout = getActivity().findViewById(R.id.RegesterFrameLayout);
        mLoginEmail = view.findViewById(R.id.loginEmail);
        mLoginPass = view.findViewById(R.id.loginPassword);
        mLoginButton = view.findViewById(R.id.loginButton);
        trial = view.findViewById(R.id.trial);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InstagramApp.class);
                startActivity(intent);
                getActivity().finish();
                /*RequestQueue queue = Volley.newRequestQueue(getContext());

                UserData u = new UserData();
                u.setName("uday");
                u.setPassword("pass");
                u.setUsername("uss");
                u.setEmail("email");
                u.setPhoneNo(new Long(100));
                u.setProfilePicUrl("pp");
                u.setBio("bio");
                u.setDOB("dob");
                JSONObject j = u.toJson(1);

                String url = "http://192.168.1.6:8080/trial";
                Log.d(TAG, "onClick: "+j.toString()+"\n"+url);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, j, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+ error.getMessage());
                    }
                });
                queue.add(request);*/
            }
        });

        mDontHaveAnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        mLoginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.setEnabled(false);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = MainActivity.BASE_URL+"login/"+mLoginEmail.getText().toString() + "/"+mLoginPass.getText().toString();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null) {
                            Log.d(TAG, "onResponse: "+response);
                            if(setUser(response)){
                                Intent intent = new Intent(getContext(), InstagramApp.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }else{
                            Toast.makeText(getContext(), "Invalid Username or password",Toast.LENGTH_LONG).show();
                            mLoginButton.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Invalid Username or password",Toast.LENGTH_LONG).show();
                        Log.d("error", "onErrorResponse: "+error.getMessage() + " "+error.getStackTrace());
                        mLoginButton.setEnabled(true);
                    }
                });

                Log.d("error", "onClick: "+url);

                queue.add(request);
            }
        });

    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(mLoginEmail.getText())){
            if(mLoginPass.length() >= 4){
                mLoginButton.setEnabled(true);
                return;
            }
        }
        mLoginButton.setEnabled(false);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mFrameLayout.getId(), fragment).commit();
    }

    public static boolean setUser(JSONObject response){
        user = new UserData();

        try {
            user.setId(response.getInt("id"));
            if(user.getId() == -1){
                return false;
            }
            user.setEmail(response.getString("email"));
            user.setUsername(response.getString("username"));
            user.setBio(response.getString("bio"));
            user.setProfilePicUrl(response.getString("profilephotoURL"));
            user.setPhoneNo(response.getLong("phoneNo"));
            user.setDOB(response.getString("dob"));
            user.setName(response.getString("name"));
            user.setPassword(response.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onResponse: " + e.getMessage());
            return false;
        }
        return true;
    }
}