package rto.example.com.rto.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.city.GetCityData;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleRequest;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleData;
import rto.example.com.rto.frameworks.signup.SignupRequest;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragRegisterVehicle extends Fragment implements View.OnClickListener {

    private EditText txtVehicleName;
    private EditText txtVehicleType;
    private EditText txtStateCode;
    private EditText txtSeriesNumber;
    private EditText txtVehicleNumber;
    private EditText txtVehicleBuyDate;
    private EditText txtPUCNumber;
    private EditText txtPUCPurchaseDate;
    private Button btnAddVehicle;
    private RelativeLayout rlLoading;

    private String STATE_ID = "", CITY_ID = "";

    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();

    private GetVehicleData getVehicleData;

    public void setGetVehicleData(GetVehicleData getVehicleData) {
        this.getVehicleData = getVehicleData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.frag_register_vehicle, container, false);
        findViews(view);
        callState();
        if (getVehicleData != null)
            setContent();
        return view;
    }


    private void findViews(View view) {
        txtVehicleName = (EditText) view.findViewById(R.id.txtVehicleName);
        txtVehicleType = (EditText) view.findViewById(R.id.txtVehicleType);
        txtStateCode = (EditText) view.findViewById(R.id.txtStateCode);
        txtSeriesNumber = (EditText) view.findViewById(R.id.txtSeriesNumber);
        txtVehicleNumber = (EditText) view.findViewById(R.id.txtVehicleNumber);
        txtVehicleBuyDate = (EditText) view.findViewById(R.id.txtVehicleBuyDate);
        txtPUCNumber = (EditText) view.findViewById(R.id.txtPUCNumber);
        txtPUCPurchaseDate = (EditText) view.findViewById(R.id.txtPUCPurchaseDate);
        btnAddVehicle = (Button) view.findViewById(R.id.btnAddVehicle);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        btnAddVehicle.setOnClickListener(this);
        txtStateCode.setOnClickListener(this);
    }

    private void setContent() {
        String name = getVehicleData.getVehicleName();
        String number = getVehicleData.getVehicleNo();
        String number_plate = getVehicleData.getVehicleNumberPlate();
        String series_no = getVehicleData.getVehicleSeriesNo();
        String type = getVehicleData.getVehicleType();
        String city_id = getVehicleData.getCityId();
        String state_id = getVehicleData.getStateId();
        String city_code = getVehicleData.getCityCode();
        String state_code = getVehicleData.getStateCode();
        String PucNumber = getVehicleData.getPucNumber();
        String purchase_date = getVehicleData.getPucPurchaseDate();
        String buy_date = getVehicleData.getBuyingDate();

        if (AppHelper.isValidString(name))
            txtVehicleName.setText(name);

        if (AppHelper.isValidString(PucNumber))
            txtPUCNumber.setText(PucNumber);

        if (AppHelper.isValidString(number))
            txtVehicleNumber.setText(number);

        if (AppHelper.isValidString(purchase_date))
            txtPUCPurchaseDate.setText(purchase_date);

        if (AppHelper.isValidString(state_id))
            txtStateCode.setText(getStateFromCode(state_id));

        if (AppHelper.isValidString(buy_date))
            txtVehicleBuyDate.setText(buy_date);

        if (AppHelper.isValidString(series_no))
            txtSeriesNumber.setText(series_no);

        if (AppHelper.isValidString(type))
            txtVehicleType.setText(type);


    }

    private String getCityFromCode(String city_id) {
        String city = "";
        if (listCity != null && listCity.size() > 0) {

            for (int i = 0; i < listCity.size(); i++) {
                if (city_id.equals(listCity.get(i).getCityId())) {
                    city = listCity.get(i).getCityName();
                    break;
                }
            }
        }
        return city;
    }

    private String getStateFromCode(String state_id) {
        String city = "";
        if (listState != null && listState.size() > 0) {

            for (int i = 0; i < listState.size(); i++) {
                if (state_id.equals(listState.get(i).getStateId())) {
                    city = listState.get(i).getStateName();
                    break;
                }
            }
        }
        return city;
    }

    private void callState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        getStateRequest.setUserId("2");
        getStateRequest.setUserType("3");
        WebAPIClient.getInstance(getActivity()).get_state(getStateRequest, new Callback<GetStateResponse>() {
            @Override
            public void onResponse(Call<GetStateResponse> call, Response<GetStateResponse> response) {
                GetStateResponse getStateResponse = response.body();
                rlLoading.setVisibility(View.GONE);
                if (getStateResponse.getFlag().equals("true")) {
                    listState.clear();
                    listState.addAll(getStateResponse.getData());
                    if (Prefs.getString(PrefsKeys.State, "").isEmpty()) {
                    } else
                        getStateId();
                   /* ArrayList<String> tmpData = new ArrayList<String>();
                    if (listState.size() > 0) {
                        for (int i = 0, count = listState.size(); i < count; i++) {
                            tmpData.add(listState.get(i).getName());
                        }

                        rlLoading.setVisibility(View.GONE);
//                        placeDialogue("state", tmpData);
                    }*/

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
        getCityRequest.setUserId("2");
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
                    //  callCity();
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
                    txtStateCode.setText(strName);
                    Prefs.putString(PrefsKeys.State, strName);
                    Prefs.putString(PrefsKeys.City, "");
                    //  txtCity.setText("");
                    callCity();
                    txtStateCode.setError(null);
                    //txtCity.setError(null);
                } else if (type.equalsIgnoreCase(Constants.CITY)) {
                    // txtCity.setText(strName);
                    // txtCity.setError(null);
                    CITY_ID = listCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.City, strName);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }


    private void callAddVehicle() {
        rlLoading.setVisibility(View.VISIBLE);

        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
        addVehicleRequest.setUserType(txtVehicleType.getText().toString());
        addVehicleRequest.setBuyingDate(txtVehicleBuyDate.getText().toString());
        addVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        addVehicleRequest.setPucPurchaseDate(txtPUCPurchaseDate.getText().toString());
        addVehicleRequest.setStateId("1");
        addVehicleRequest.setCityId("1");
        addVehicleRequest.setVehicleName(txtVehicleName.getText().toString());


        WebAPIClient.getInstance(getActivity()).add_vehicle(addVehicleRequest, new Callback<AddVehicleResponse>() {
            @Override
            public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                AddVehicleResponse addVehicleResponse = response.body();
                if (addVehicleResponse.getFlag().equals("true")) {
                    rlLoading.setVisibility(View.GONE);

                    getActivity().finish();
                    startActivity(new Intent(getActivity(), ActHomeUser.class));

                } else if (addVehicleResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void callEditVehicle() {

        EditVehicleRequest editVehicleRequest = new EditVehicleRequest();
        editVehicleRequest.setVehicleId(getVehicleData.getVehicleId());
        editVehicleRequest.setUserType(txtVehicleType.getText().toString());
        editVehicleRequest.setBuyingDate(txtVehicleBuyDate.getText().toString());
        editVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        editVehicleRequest.setPucPurchaseDate(txtPUCPurchaseDate.getText().toString());
        editVehicleRequest.setStateId("1");
        editVehicleRequest.setCityId("1");
        editVehicleRequest.setVehicleName(txtVehicleName.getText().toString());
        rlLoading.setVisibility(View.VISIBLE);
        WebAPIClient.getInstance(getActivity()).edit_vehicle(editVehicleRequest, new Callback<EditVehicleResponse>() {
            @Override
            public void onResponse(Call<EditVehicleResponse> call, Response<EditVehicleResponse> response) {
                EditVehicleResponse getEditVehicleResponse = response.body();
                if (getEditVehicleResponse.getFlag().equals("true")) {
                    rlLoading.setVisibility(View.GONE);

                    getActivity().finish();
                    startActivity(new Intent(getActivity(), ActHomeUser.class));

                } else if (getEditVehicleResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<EditVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Log.e("err_Edit_vehicle", t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == btnAddVehicle) {

            callAddVehicle();
        } else if (v == txtStateCode) {
            openStateDilaog();
        }
    }

}
