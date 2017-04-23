package rto.example.com.rto.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.sql.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeOfficer;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleRequest;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleResponse;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.city.GetCityData;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleData;
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragAddTawVehicle extends Fragment implements View.OnClickListener {

private ActHomeOfficer root;

    private EditText txtVehicleName;
    private EditText txtVehicleState;
    private EditText txtVehicleCity;
    private EditText txtSeriesNumber;
    private EditText txtVehicleNumber;
    private EditText txtState;
    private EditText txtCity;
    private EditText txtAddress;
    private EditText txtVehicleType;
    private RelativeLayout rlLoading;
    private Button btnAddVehicle;
    ArrayList<String> tmpData = new ArrayList<>();
    private String STATE_ID = "", CITY_ID = "";
    private String VEHICLE_STATE_ID = "", VEHICEL_CITY_ID = "";
    private ArrayList<GetStateData> listVehicleState = new ArrayList<>();
    private ArrayList<GetCityData> listVehicleCity = new ArrayList<>();
    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();


    private GetOfficerTawVehicleData getOfficerTawVehicleData;
    private boolean isRegister;


    public void setGetOfficerTawVehicleData(GetOfficerTawVehicleData getOfficerTawVehicleData) {
        this.getOfficerTawVehicleData = getOfficerTawVehicleData;
    }

    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_add_taw_vehicle, container, false);
        findViews(view);
        root.setActTitle("Add Taw Vehicle");
        tmpData.add("Car");
        tmpData.add("Bike");
        callVehicleState();
        callState();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActHomeOfficer)activity;
    }

    private void findViews(View view) {
        txtVehicleName = (EditText) view.findViewById(R.id.txtVehicleName);
        txtVehicleState = (EditText) view.findViewById(R.id.txtVehicleState);
        txtVehicleCity = (EditText) view.findViewById(R.id.txtVehicleCity);
        txtSeriesNumber = (EditText) view.findViewById(R.id.txtSeriesNumber);
        txtVehicleNumber = (EditText) view.findViewById(R.id.txtVehicleNumber);
        txtState = (EditText) view.findViewById(R.id.txtState);
        txtCity = (EditText) view.findViewById(R.id.txtCityCode);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        txtVehicleType = (EditText) view.findViewById(R.id.txtVehicleType);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        btnAddVehicle = (Button) view.findViewById(R.id.btnAddVehicle);

        txtSeriesNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnAddVehicle.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtVehicleCity.setOnClickListener(this);
        txtVehicleState.setOnClickListener(this);
        txtVehicleType.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnAddVehicle) {
            validateform();

        } else if (v == txtVehicleState) {
            openVehicleStateDilaog();

        } else if (v == txtVehicleCity) {
            openVehicleCityDialog();

        } else if (v == txtState) {
            openStateDilaog();

        } else if (v == txtCity) {
            openCityDialog();

        } else if (v == txtVehicleType) {
            vehicleTypeDialog();
        }
    }

    private void validateform() {
        boolean vehicleStateId = AppHelper.CheckEditText(txtVehicleState);
        boolean vehicleCityId = AppHelper.CheckEditText(txtVehicleCity);
        boolean seriesNo = AppHelper.CheckEditText(txtVehicleCity);
        boolean vehicleNumber = AppHelper.CheckEditText(txtVehicleNumber);
        boolean stateId = AppHelper.CheckEditText(txtState);
        boolean cityId = AppHelper.CheckEditText(txtCity);
        boolean tawAddress = AppHelper.CheckEditText(txtAddress);
        boolean type = AppHelper.CheckEditText(txtVehicleType);

        if (vehicleStateId && vehicleCityId && seriesNo && vehicleNumber && stateId && cityId && tawAddress && type) {
            callAddTawVehicle();
        }

    }

    private void callAddTawVehicle() {
        rlLoading.setVisibility(View.VISIBLE);

        AddTawVehicleRequest addTawVehicleRequest = new AddTawVehicleRequest();
        addTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        addTawVehicleRequest.setUserType(Constants.TYPE_OFFICER);
        addTawVehicleRequest.setStateId(STATE_ID);
        addTawVehicleRequest.setCityId(CITY_ID);
        addTawVehicleRequest.setVehicleStateId(VEHICLE_STATE_ID);
        addTawVehicleRequest.setVehicleCityId(VEHICEL_CITY_ID);
        addTawVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        addTawVehicleRequest.setVehicleType(txtVehicleType.getText().toString().equals("Car") ? "2" : "1");
        addTawVehicleRequest.setVehicleNo(txtVehicleNumber.getText().toString());
        addTawVehicleRequest.setCheckSecondTime("1");
        addTawVehicleRequest.setTawAddress(txtAddress.getText().toString());


        WebAPIClient.getInstance(getActivity()).add_taw_vehicle(addTawVehicleRequest, new Callback<AddTawVehicleResponse>() {
            @Override
            public void onResponse(Call<AddTawVehicleResponse> call, Response<AddTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                AddTawVehicleResponse addTawVehicleResponse = response.body();
                if (addTawVehicleResponse.getFlag().equals("true")) {
                    Toast.makeText(getActivity(), "Vehicle registered successfully!", Toast.LENGTH_SHORT).show();

                    txtVehicleState.setText("");
                    txtVehicleCity.setText("");
                    txtSeriesNumber.setText("");
                    txtVehicleNumber.setText("");
                    txtState.setText("");
                    txtCity.setText("");
                    txtAddress.setText("");
                    txtVehicleType.setText("");
                    txtVehicleType.setText("");
                    txtVehicleName.setText("");
                    //getActivity().getSupportFragmentManager().popBackStack();
                } else if (addTawVehicleResponse.getFlag().equals("false")) {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //----------------------------------- vehicle  state-city ---------------------------------------

    private void callVehicleState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        getStateRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getStateRequest.setUserType(Constants.TYPE_OFFICER);
        WebAPIClient.getInstance(getActivity()).get_state(getStateRequest, new Callback<GetStateResponse>() {
            @Override
            public void onResponse(Call<GetStateResponse> call, Response<GetStateResponse> response) {
                rlLoading.setVisibility(View.GONE);
                GetStateResponse getStateResponse = response.body();
                if (getStateResponse.getFlag().equals("true")) {
                    listVehicleState.clear();
                    listVehicleState.addAll(getStateResponse.getData());
                    if (!Prefs.getString(PrefsKeys.State, "").isEmpty())
                        getVehicleStateId();
                } else if (getStateResponse.getFlag().equals("false")) {
                }
            }

            @Override
            public void onFailure(Call<GetStateResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "state_err" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callVehicleCity() {
        rlLoading.setVisibility(View.VISIBLE);
        GetCityRequest getCityRequest = new GetCityRequest();
        getCityRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getCityRequest.setUserType(Constants.TYPE_OFFICER);
        getCityRequest.setStateId(VEHICLE_STATE_ID);
        WebAPIClient.getInstance(getActivity()).get_city(getCityRequest, new Callback<GetCityResponse>() {
            @Override
            public void onResponse(Call<GetCityResponse> call, Response<GetCityResponse> response) {
                rlLoading.setVisibility(View.GONE);
                GetCityResponse getCityResponse = response.body();
                listVehicleCity.clear();
                if (getCityResponse.getFlag().equals("true")) {
                    listVehicleCity.addAll(getCityResponse.getData());
                    /*ArrayList<String> tmpData = new ArrayList<>();
                    if (listCity.size() > 0) {
                        for (int i = 0, count = listCity.size(); i < count; i++) {
                            tmpData.add(listCity.get(i).getName());
                        }
                        rlLoading.setVisibility(View.GONE);
//                        placeDialogue("city", tmpData);
                    }*/
                } else if (getCityResponse.getFlag().equals("false")) {
                }
            }

            @Override
            public void onFailure(Call<GetCityResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getVehicleStateId() {
        //If no state is selected
        if (listVehicleState.size() > 0) {
            for (int i = 0, count = listVehicleState.size(); i < count; i++) {
                if (Prefs.getString(PrefsKeys.State, "").equalsIgnoreCase(listVehicleState.get(i).getStateName())) {
                    VEHICLE_STATE_ID = listVehicleState.get(i).getStateId();
                    callVehicleCity();
                    break;
                }
            }
        }
    }

    private void openVehicleStateDilaog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listVehicleState.size() > 0) {
            if (Prefs.getString(PrefsKeys.State, "").isEmpty()) {
                for (int i = 0, count = listVehicleState.size(); i < count; i++) {
                    tmpData.add(listVehicleState.get(i).getStateName());
                }
            } else {
                for (int i = 0, count = listVehicleState.size(); i < count; i++) {
                    tmpData.add(listVehicleState.get(i).getStateName());
                    if (listVehicleState.get(i).getStateName().equalsIgnoreCase(Prefs.getString(PrefsKeys.State, ""))) {
                        selectedOption = i;
                    }
                }
            }
        }
        placeVehicleDialogue(Constants.STATE, tmpData, selectedOption);
    }

    private void openVehicleCityDialog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listVehicleCity.size() > 0) {
            if (Prefs.getString(PrefsKeys.City, "").isEmpty()) {
                for (int i = 0, count = listVehicleCity.size(); i < count; i++) {
                    tmpData.add(listVehicleCity.get(i).getCityName());
                }
            } else {
                for (int i = 0, count = listVehicleCity.size(); i < count; i++) {
                    tmpData.add(listVehicleCity.get(i).getCityName());
                    if (listVehicleCity.get(i).getCityName().equalsIgnoreCase(Prefs.getString(PrefsKeys.City, ""))) {
                        selectedOption = i;
                    }
                }
            }
        }
        placeVehicleDialogue(Constants.CITY, tmpData, selectedOption);
    }

    private void placeVehicleDialogue(final String type, ArrayList<String> tmpData, int selectedOption) {
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
                    VEHICLE_STATE_ID = listVehicleState.get(which).getStateId();
                    txtVehicleState.setText(strName);
                    Prefs.putString(PrefsKeys.State, strName);
                    Prefs.putString(PrefsKeys.City, "");
                    txtVehicleCity.setText("");
                    callCity();
                    txtVehicleCity.setError(null);
                    txtVehicleCity.setError(null);
                } else if (type.equalsIgnoreCase(Constants.CITY)) {
                    txtVehicleCity.setText(listVehicleCity.get(which).getCityCode());
                    txtVehicleCity.setError(null);
                    VEHICEL_CITY_ID = listVehicleCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.City, strName);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    //-------------------------------- user state-city ------------------------------------------------

    private void callState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        getStateRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getStateRequest.setUserType(Constants.TYPE_OFFICER);
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
                // Toast.makeText(getActivity(), "state_err" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callCity() {
        rlLoading.setVisibility(View.VISIBLE);
        GetCityRequest getCityRequest = new GetCityRequest();
        getCityRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getCityRequest.setUserType(Constants.TYPE_OFFICER);
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
                    txtCity.setText(listVehicleCity.get(which).getCityCode());
                    txtCity.setError(null);
                    CITY_ID = listCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.City, strName);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }


    private void vehicleTypeDialog() {
        int selectedOption = -1;
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        builderSingle.setTitle("Select vehicle type :-");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, tmpData);
        if (txtVehicleType.getText().toString().trim().equalsIgnoreCase("Car")) {
            selectedOption = 0;
        } else if (txtVehicleType.getText().toString().trim().equalsIgnoreCase("Bike")) {
            selectedOption = 1;
        }

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
                txtVehicleType.setText(strName);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }


}
