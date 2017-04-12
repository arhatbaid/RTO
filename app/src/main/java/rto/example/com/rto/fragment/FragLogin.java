package rto.example.com.rto.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.activity.ActLoginSignUp;
import rto.example.com.rto.frameworks.signin.SigninRequest;
import rto.example.com.rto.frameworks.signin.SigninResponse;
import rto.example.com.rto.frameworks.signup.SignupRequest;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.webhelper.WebAPIClient;


public class FragLogin extends Fragment implements View.OnClickListener {

    private ActLoginSignUp root;

    private EditText txtEmail;
    private EditText txtPassword;
    private RadioButton rbUser;
    private RadioButton rbOfficer;
    private Button btnLogin;
    private TextView lblSignUp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActLoginSignUp) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login, container, false);
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
        rbUser = (RadioButton) view.findViewById(R.id.rbUser);
        rbOfficer = (RadioButton) view.findViewById(R.id.rbOfficer);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        lblSignUp = (TextView) view.findViewById(R.id.lblSignUp);

        rbUser.setOnClickListener(this);
        rbOfficer.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        lblSignUp.setOnClickListener(this);
    }


//    private void callSignIn(SigninRequest signinRequest) {
//       // rlLoading.setVisibility(View.VISIBLE);
//        WebAPIClient.getInstance(getActivity()).login_user(signinRequest, new Callback<SigninResponse>() {
//            @Override
//            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
//                SigninResponse getStateResponse = response.body();
//                if (getStateResponse.getFlag().equals("true")) {
//                   // rlLoading.setVisibility(View.GONE);
//
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(),ActHomeUser.class));
//
//                } else if (getStateResponse.getFlag().equals("false")) {
//                    rlLoading.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SigninResponse> call, Throwable t) {
//                rlLoading.setVisibility(View.GONE);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        if (v == rbUser) {

        } else if (v == rbOfficer) {

        }  else if (v == btnLogin) {
            startActivity(new Intent(getActivity(), ActHomeUser.class));
        } else if (v == lblSignUp) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            FragSignUp fragSignUp = new FragSignUp();
            ft.addToBackStack(FragSignUp.class.getName());
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                    R.anim.slide_in_right, R.anim.slide_out_right);
            ft.replace(R.id.fragContainer, fragSignUp, FragSignUp.class.getName());
            ft.commit();
        }
    }
}
