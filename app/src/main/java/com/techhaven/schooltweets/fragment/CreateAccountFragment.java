package com.techhaven.schooltweets.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techhaven.schooltweets.R;


public class CreateAccountFragment extends Fragment {
    EditText etName, etUsername, etEmail, etPassword, etRPassword;
    TextView txtBack;
    Button btnSignUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        txtBack = (TextView) view.findViewById(R.id.backButton);
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etRPassword = (EditText) view.findViewById(R.id.etRePassword);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btnSignUp.setOnClickListener(signUp);
        return view;
    }

    View.OnClickListener signUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validateInput();
        }
    };

    boolean validateInput() {
        return true;
    }
}
