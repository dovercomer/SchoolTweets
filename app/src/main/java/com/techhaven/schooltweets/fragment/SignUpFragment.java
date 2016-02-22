package com.techhaven.schooltweets.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techhaven.schooltweets.R;
import com.techhaven.schooltweets.activity.CreateAccountActivity;
import com.techhaven.schooltweets.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    LinearLayout layoutContainer;
    TextView txtSignUp;
    Button btnSkip;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        layoutContainer = (LinearLayout) view.findViewById(R.id.layoutContainer);
        txtSignUp = (TextView) view.findViewById(R.id.txtSignUp);
        btnSkip = (Button) view.findViewById(R.id.btnSkip);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}
