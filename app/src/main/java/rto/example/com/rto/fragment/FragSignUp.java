package rto.example.com.rto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;

public class FragSignUp extends Fragment implements View.OnClickListener {


    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtAddress;
    private EditText txtPhoneNumber;
    private EditText txtState;
    private EditText txtCity;
    private RadioButton rbUser;
    private RadioButton rbOfficer;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button btnSignUp;
    private TextView lblLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        findViews();
    }


    private void findViews() {
        View view = getView();
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtPassword = (EditText) view.findViewById(R.id.txtPassword);
        txtFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        txtPhoneNumber = (EditText) view.findViewById(R.id.txtPhoneNumber);
        txtState = (EditText) view.findViewById(R.id.txtState);
        txtCity = (EditText) view.findViewById(R.id.txtCity);
        rbUser = (RadioButton) view.findViewById(R.id.rbUser);
        rbOfficer = (RadioButton) view.findViewById(R.id.rbOfficer);
        rbFemale = (RadioButton) view.findViewById(R.id.rbFemale);
        rbMale = (RadioButton) view.findViewById(R.id.rbMale);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        lblLogin = (TextView) view.findViewById(R.id.lblLogin);

        rbUser.setOnClickListener(this);
        rbOfficer.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        lblLogin.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == rbUser) {

            // Handle clicks for rbUser
        } else if (v == rbOfficer) {
            // Handle clicks for rbOfficer
        } else if (v == btnSignUp) {
            startActivity(new Intent(getActivity(), ActHomeUser.class));
        } else if (v == rbFemale) {
            // Handle clicks for rbOfficer
        } else if (v == rbMale) {
            // Handle clicks for btnSignUp
        } else if (v == lblLogin) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}
