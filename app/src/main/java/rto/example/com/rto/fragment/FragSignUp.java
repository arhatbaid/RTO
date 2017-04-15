package rto.example.com.rto.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.frameworks.city.GetCityData;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.signup.SignupRequest;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragSignUp extends Fragment implements View.OnClickListener {


    private RadioGroup rgSex;
    private RadioGroup rgUserType;
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
    private RelativeLayout rlLoading;

    private String STATE_ID = "", CITY_ID = "";
    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_signup, container, false);
        findViews(view);
        callState();
        return view;
    }

    private void findViews(View view) {
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
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        rgSex = (RadioGroup) view.findViewById(R.id.rgSex);
        rgUserType = (RadioGroup) view.findViewById(R.id.rgUserType);

        rbUser.setOnClickListener(this);
        rbOfficer.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        lblLogin.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        txtState.setOnClickListener(this);
    }


    private void callState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        getStateRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getStateRequest.setUserType("3");
        WebAPIClient.getInstance(getActivity()).get_state(getStateRequest, new Callback<GetStateResponse>() {
            @Override
            public void onResponse(Call<GetStateResponse> call, Response<GetStateResponse> response) {
                GetStateResponse getStateResponse = response.body();
                if (getStateResponse.getFlag().equals("true")) {
                    listState.clear();
                    listState.addAll(getStateResponse.getData());
                    if (Prefs.getString(PrefsKeys.State, "").isEmpty())
                        rlLoading.setVisibility(View.GONE);
                    else
                        getStateId();
                } else if (getStateResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetStateResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "state_err" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callCity() {
        rlLoading.setVisibility(View.VISIBLE);
        GetCityRequest getCityRequest = new GetCityRequest();
        getCityRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getCityRequest.setUserType("3");
        getCityRequest.setStateId(STATE_ID);
        WebAPIClient.getInstance(getActivity()).get_city(getCityRequest, new Callback<GetCityResponse>() {
            @Override
            public void onResponse(Call<GetCityResponse> call, Response<GetCityResponse> response) {
                GetCityResponse getCityResponse = response.body();
                listCity.clear();
                if (getCityResponse.getFlag().equals("true")) {
                    listCity.addAll(getCityResponse.getData());
                    rlLoading.setVisibility(View.GONE);
                    /*ArrayList<String> tmpData = new ArrayList<>();
                    if (listCity.size() > 0) {
                        for (int i = 0, count = listCity.size(); i < count; i++) {
                            tmpData.add(listCity.get(i).getName());
                        }
                        rlLoading.setVisibility(View.GONE);
//                        placeDialogue("city", tmpData);
                    }*/
                } else if (getCityResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetCityResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getStateId() {
        //If no state is selected
        if (listState.size() > 0) {
            for (int i = 0, count = listState.size(); i < count; i++) {
                if (Prefs.getString(PrefsKeys.State, "").equalsIgnoreCase(listState.get(i).getStateName())) {
                    STATE_ID = listState.get(i).getStateId();
                    callCity();
                    break;
                }
            }
        }
    }

    private void openStateDilaog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listState.size() > 0) {
            if (Prefs.getString(PrefsKeys.State, "").isEmpty()) {
                for (int i = 0, count = listState.size(); i < count; i++) {
                    tmpData.add(listState.get(i).getStateName());
                }
            } else {
                for (int i = 0, count = listState.size(); i < count; i++) {
                    tmpData.add(listState.get(i).getStateName());
                    if (listState.get(i).getStateName().equalsIgnoreCase(Prefs.getString(PrefsKeys.State, ""))) {
                        selectedOption = i;
                    }
                }
            }
        }
        placeDialogue(Constants.STATE, tmpData, selectedOption);
    }

    private void openCityDialog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listCity.size() > 0) {
            if (Prefs.getString(PrefsKeys.City, "").isEmpty()) {
                for (int i = 0, count = listCity.size(); i < count; i++) {
                    tmpData.add(listCity.get(i).getCityName());
                }
            } else {
                for (int i = 0, count = listCity.size(); i < count; i++) {
                    tmpData.add(listCity.get(i).getCityName());
                    if (listCity.get(i).getCityName().equalsIgnoreCase(Prefs.getString(PrefsKeys.City, ""))) {
                        selectedOption = i;
                    }
                }
            }
        }
        placeDialogue(Constants.CITY, tmpData, selectedOption);
    }

    private void placeDialogue(final String type, ArrayList<String> tmpData, int selectedOption) {
        //Todo show the last selected option
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        builderSingle.setTitle("Select " + type + " :-");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, tmpData);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builderSingle.setSingleChoiceItems(arrayAdapter, selectedOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                if (type.equalsIgnoreCase(Constants.STATE)) {
                    STATE_ID = listState.get(which).getStateId();
                    txtState.setText(strName);
                    Prefs.putString(PrefsKeys.State, strName);
                    Prefs.putString(PrefsKeys.City, "");
                    txtCity.setText("");
                    callCity();
                    txtState.setError(null);
                    txtCity.setError(null);
                } else if (type.equalsIgnoreCase(Constants.CITY)) {
                    txtCity.setText(strName);
                    txtCity.setError(null);
                    CITY_ID = listCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.City, strName);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void validateForm() {
        boolean fname = AppHelper.CheckEditText(txtFirstName);
        boolean lname = AppHelper.CheckEditText(txtLastName);

        boolean male = rbFemale.isChecked();
        boolean female = rbMale.isChecked();
        if (!(male || female)) {
            Toast.makeText(getActivity(), "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        //No need to do
       /* boolean officer = rbOfficer.isChecked();
        boolean user = rbUser.isChecked();
        if (!(officer || user)) {
            Toast.makeText(getActivity(), "Please select user type", Toast.LENGTH_SHORT).show();
            //YoYo.with(Techniques.Shake).duration(400).playOn(rdoGroupSex);
            return;
        }*/

//        boolean value5 = AppHelper.CheckEditText(txtCountry);
        boolean state = AppHelper.CheckEditText(txtState);
        boolean city = AppHelper.CheckEditText(txtCity);
        //  boolean value2 = checkTc.isChecked();

//        if (!value2) {
//            YoYo.with(Techniques.Shake).duration(400).playOn(checkTc);
//            return;
//        }

        if (fname && lname && state && city) {
            String SEX = "";
            String TYPE = "";
            int selectedSexId = rgSex.getCheckedRadioButtonId();
            int selectedTypeId = rgUserType.getCheckedRadioButtonId();

            if (selectedSexId == rbMale.getId()) {
                SEX = "m";
            } else if (selectedSexId == rbFemale.getId()) {
                SEX = "f";
            }


            SignupRequest signupRequest = new SignupRequest();

            signupRequest.setFirstname(txtFirstName.getText().toString().trim());
            signupRequest.setLastname(txtLastName.getText().toString().trim());
            signupRequest.setStateId(STATE_ID);
            signupRequest.setCityId(CITY_ID);
            signupRequest.setAddress(txtAddress.getText().toString().trim());
            signupRequest.setUserType("3");
            signupRequest.setGender(SEX);
            signupRequest.setPhone(txtPhoneNumber.getText().toString().trim());
            signupRequest.setEmailId(txtEmail.getText().toString().trim());
            signupRequest.setPassword(txtPassword.getText().toString().trim());

            callSignUp(signupRequest);
        }
    }

    private void callSignUp(SignupRequest signupRequest) {
        rlLoading.setVisibility(View.VISIBLE);
        WebAPIClient.getInstance(getActivity()).register_user(signupRequest, new Callback<SignupRespose>() {
            @Override
            public void onResponse(Call<SignupRespose> call, Response<SignupRespose> response) {
                SignupRespose getStateResponse = response.body();
                if (getStateResponse.getFlag().equals("true")) {
                    rlLoading.setVisibility(View.GONE);

                    startActivity(new Intent(getActivity(), ActHomeUser.class));
                    getActivity().finish();

                } else if (getStateResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SignupRespose> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == rbUser) {

            // Handle clicks for rbUser
        } else if (v == rbOfficer) {
            // Handle clicks for rbOfficer
        } else if (v == btnSignUp) {
            //startActivity(new Intent(getActivity(), ActHomeUser.class));
            validateForm();
        } else if (v == rbFemale) {
            // Handle clicks for rbOfficer
        } else if (v == rbMale) {
            // Handle clicks for btnSignUp
        } else if (v == lblLogin) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (v == txtCity) {
            openCityDialog();
        } else if (v == txtState) {
            openStateDilaog();
        }
    }

}
