package com.example.instagram;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Random;


public class SignUpFragment extends Fragment {


    private TextView mAlreadyHaveAnAcc;
    private FrameLayout mFrameLayout;
    private LinearLayout mPhonelinearLayout;
    private LinearLayout mEmaillinearLayout;
    private TextView mPhone;
    private TextView mEmail;
    private View mPhoneDivider;
    private View mEmailDivider;
    private EditText mSignUpPhoneNo;
    private EditText mSignUpEmail;
    private Button mNextButton;

    private static boolean onPhone = true;
    public SignUpFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        mFrameLayout = getActivity().findViewById(R.id.RegesterFrameLayout);
        mAlreadyHaveAnAcc = view.findViewById(R.id.alreadyHaveAnAcc);
        onPhone = true;
        mPhonelinearLayout = view.findViewById(R.id.phoneLinearLayout);
        mEmaillinearLayout = view.findViewById(R.id.emailLinearLayout);
        mPhone = view.findViewById(R.id.signUpPhone);
        mEmail = view.findViewById(R.id.signupEmail);
        mPhone.setTypeface(null, Typeface.BOLD);
        mPhoneDivider = view.findViewById(R.id.phoneDivider);
        mPhoneDivider.setBackgroundColor(Color.BLACK);
        mEmailDivider = view.findViewById(R.id.emailDivider);
        mSignUpEmail = view.findViewById(R.id.signupEmailId);
        mSignUpPhoneNo = view.findViewById(R.id.signupPhoneNo);
        mNextButton = view.findViewById(R.id.nextButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAlreadyHaveAnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onPhone){
                    mEmail.setTypeface(null, Typeface.NORMAL);
                    mPhoneDivider.setBackgroundColor(Color.BLACK);
                    mPhone.setTypeface(null, Typeface.BOLD);
                    mEmailDivider.setBackgroundColor(Color.rgb(192,192,192));
                    mEmaillinearLayout.setVisibility(View.INVISIBLE);
                    mPhonelinearLayout.setVisibility(View.VISIBLE);
                    onPhone = true;
                }
            }
        });
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPhone){
                    mEmail.setTypeface(null, Typeface.BOLD);
                    mEmailDivider.setBackgroundColor(Color.BLACK);
                    mPhone.setTypeface(null, Typeface.NORMAL);
                    mPhoneDivider.setBackgroundColor(Color.rgb(192,192,192));
                    mEmaillinearLayout.setVisibility(View.VISIBLE);
                    mPhonelinearLayout.setVisibility(View.INVISIBLE);
                    onPhone = false;
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = MainActivity.BASE_URL+"checkuser/"+(onPhone ? ("pno/"+mSignUpPhoneNo.getText().toString()) : ("email/"+mSignUpEmail.getText().toString()));
                mNextButton.setEnabled(false);
                Log.d("error", "onClick: next butto clicked\n"+url);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("error", "onResponse: "+response);
                        if(response!=null) {
                            if (SignInFragment.setUser(response)) {
                                Toast.makeText(getContext(), "This " + (onPhone ? "Phone no" : "Email id") + " is already registered", Toast.LENGTH_SHORT).show();
                                customDialogForExistingUser();

                            } else {
                                Toast.makeText(getContext(), "you are good to go", Toast.LENGTH_LONG).show();
                                if(onPhone){
                                    SignInFragment.user.setPhoneNo(Long.parseLong(mSignUpPhoneNo.getText().toString()));
                                }else{
                                    SignInFragment.user.setEmail(mSignUpEmail.getText().toString());
                                }
                                customDialogForNewUser();
                            }
                        }else{
                            Toast.makeText(getContext(), "This " + (onPhone ? "Phone no" : "Email id") + " is already registered", Toast.LENGTH_SHORT).show();
                            customDialogForExistingUser();
                        }
                        mNextButton.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error:"+error.getMessage(), Toast.LENGTH_LONG);
                        mNextButton.setEnabled(true);
                    }
                });
                queue.add(request);
            }
        });
    }

    private void customDialogForNewUser(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.new_user_dialog, null);
        Button check = (Button) view.findViewById(R.id.check);
        EditText input = (EditText) view.findViewById(R.id.editText);
        final TextView generate = (TextView) view.findViewById(R.id.numberText);
        alert.setView(view);
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Random random = new Random();
        String s = new Integer(random.nextInt(10000)).toString();
        generate.setText(s);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generate.getText().toString().equals(input.getText().toString())){
                    Toast.makeText(getContext(), "You are good to go ahead", Toast.LENGTH_LONG).show();
                    setFragment(new EnterDetailsFragment());
                    alertDialog.dismiss();
                }else{
                    generate.setText(new Integer(random.nextInt(10000)).toString());
                    Toast.makeText(getContext(), "The numbers do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.show();
    }

    private void customDialogForExistingUser(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.existing_user_dialog, null);
        TextView exist = view.findViewById(R.id.textView1OfExistingUser);
        TextView loginas = view.findViewById(R.id.logInAs);
        TextView create = view.findViewById(R.id.createnewacc);

        alert.setView(view);
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        String username = SignInFragment.user.getUsername();
        exist.setText("LogIn as "+username+"? It looks like you already have an instagram Account");

        loginas.setText("Continue as "+username);
        loginas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InstagramApp.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Go ahead", Toast.LENGTH_LONG).show();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mFrameLayout.getId(), fragment).commit();
    }
}