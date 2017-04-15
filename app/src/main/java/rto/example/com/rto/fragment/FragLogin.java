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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.activity.ActLoginSignUp;
import rto.example.com.rto.frameworks.signin.SigninRequest;
import rto.example.com.rto.frameworks.signin.SigninResponse;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;


public class FragLogin extends Fragment implements View.OnClickListener {

    private ActLoginSignUp root;

    private EditText txtEmail;
    private EditText txtPassword;
    private RadioButton rbUser;
    private RadioButton rbOfficer;
    private Button btnLogin;
    private TextView lblSignUp;
    private RelativeLayout rlLoading;

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
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        rbUser.setOnClickListener(this);
        rbOfficer.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        lblSignUp.setOnClickListener(this);
    }


    private void callSignIn() {
        rlLoading.setVisibility(View.VISIBLE);
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmailId(txtEmail.getText().toString().trim());
        signinRequest.setPassword(txtPassword.getText().toString().trim());
        signinRequest.setUserType(rbUser.isChecked() ? "3" : "2");
        WebAPIClient.getInstance(getActivity()).login_user(signinRequest, new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                SigninResponse signinResponse = response.body();
                rlLoading.setVisibility(View.GONE);
                if (signinResponse.getFlag().equals("true")) {
                    Prefs.putString(PrefsKeys.USERID, signinResponse.getData().getUserId());
                    Prefs.putString(PrefsKeys.FIRSTNAME, signinResponse.getData().getFirstname());
                    Prefs.putString(PrefsKeys.LASTNAME, signinResponse.getData().getLastname());
                    Prefs.putString(PrefsKeys.Birthdate, signinResponse.getData().getBirthdate());
                    Prefs.putString(PrefsKeys.GENDER, signinResponse.getData().getGender());
                    Prefs.putString(PrefsKeys.ADDRESS, signinResponse.getData().getAddress());
                    Prefs.putString(PrefsKeys.City, signinResponse.getData().getCityId());
                    Prefs.putString(PrefsKeys.State, signinResponse.getData().getStateId());
                    Prefs.putString(PrefsKeys.MOBILE, signinResponse.getData().getPhone());
                    Prefs.putString(PrefsKeys.USER_TYPE, signinResponse.getData().getUserType());
                    Prefs.putString(PrefsKeys.status, signinResponse.getData().getStatus());
                    startActivity(new Intent(getActivity(), ActHomeUser.class));
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),ActHomeUser.class));
                }
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == rbUser) {

        } else if (v == rbOfficer) {

        } else if (v == btnLogin) {
            if (isValid()) {
                callSignIn();
            }
        } else if (v == lblSignUp) {
            Prefs.clear();//Clear all previous data.
            FragSignUp fragSignUp = new FragSignUp();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(FragSignUp.class.getName());
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                    R.anim.slide_in_right, R.anim.slide_out_right);
            ft.replace(R.id.fragContainer, fragSignUp, FragSignUp.class.getName());
            ft.commit();
        }
    }

    private boolean isValid() {
        if (txtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(root, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(root, "Please enter valid password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
