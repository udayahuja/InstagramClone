package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EnterDetailsFragment extends Fragment {


    EditText name, username, password;
    ImageView profilePhoto;
    Button next;
    private final String TAG = "error";

    public EnterDetailsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_details, container, false);
        name = view.findViewById(R.id.details_name);
        username = view.findViewById(R.id.detals_username);
        password = view.findViewById(R.id.details_password);
        profilePhoto = view.findViewById(R.id.details_image);
        next = view.findViewById(R.id.details_button);
        next.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.addTextChangedListener(new TextWatcher() {
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
        username.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setEnabled(false);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = MainActivity.BASE_URL+"add";
                UserData user = SignInFragment.user;
                user.setName(name.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                Log.d(TAG, "onClick: next button of enter details");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user.toJson(1), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user.setId(response.getInt("id"));
                            Log.d(TAG, "onResponse: the id assigned is:"+user.getId());
                            InstagramApp.goToHomePage = false;
                            Intent intent = new Intent(getContext(), InstagramApp.class);
                            startActivity(intent);
                            getActivity().finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: username already exists");
                        username.setError("This username already exists");
                        next.setEnabled(true);
                    }
                });
                queue.add(request);
            }
        });


    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(username.getText())){
                if (password.getText().length() >= 5){
                    next.setEnabled(true);
                    return;
                }
            }
        }
        next.setEnabled(false);
    }
}