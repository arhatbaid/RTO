package rto.example.com.rto.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeOfficer;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.frameworks.city.GetCityData;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationData;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleData;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleRequest;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleResponse;
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

/**
 * Created by ridz1 on 14/04/2017.
 */

public class FragSearchTawVehicle extends Fragment implements View.OnClickListener {


    private ActHomeUser rootHome;
    private ActHomeOfficer rootOfficer;
    private EditText txtState;
    private EditText txtCity;
    private EditText txtSeriesNumber;
    private EditText txtVehicleNumber;
    private RelativeLayout rlLoading;
    private Button btnSearch;
    private Button btnNearestPoliceStation;

    private LinearLayout lytFound;
    private TextView lblVehicleNumber;
    private TextView lblSeriesNumber;
    private TextView lblAddress;
    private TextView lblTime;

    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();
    private ArrayList<GetNearPoliceStationData> arrPoliceStation = new ArrayList<>();

    private String STATE_ID = "";
    private String CITY_ID = "";

    private boolean isFromOfficer;

    public void setFromOfficer(boolean fromOfficer) {
        isFromOfficer = fromOfficer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_taw_vehicle, container, false);
        findViews(view);
        if (isFromOfficer)
            rootOfficer.setActTitle("Search Vehicle");
        else
            rootHome.setActTitle("Search Vehicle");
        callState();
        return view;
    }

    private void findViews(View view) {
        txtState = (EditText) view.findViewById(R.id.txtState);
        txtCity = (EditText) view.findViewById(R.id.txtCity);
        txtSeriesNumber = (EditText) view.findViewById(R.id.txtSeriesNumber);
        txtVehicleNumber = (EditText) view.findViewById(R.id.txtVehicleNumber);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnNearestPoliceStation = (Button) view.findViewById(R.id.btnNearestPoliceStation);

        lytFound = (LinearLayout) view.findViewById(R.id.lytFound);
        lblVehicleNumber = (TextView) view.findViewById(R.id.lblVehicleNumber);
        lblSeriesNumber = (TextView) view.findViewById(R.id.lblSeriesNumber);
        lblAddress = (TextView) view.findViewById(R.id.lblAddress);
        lblTime = (TextView) view.findViewById(R.id.lblTime);

        txtSeriesNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btnSearch.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        btnNearestPoliceStation.setOnClickListener(this);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (isFromOfficer)
            rootOfficer = (ActHomeOfficer) activity;
        else
            rootHome = (ActHomeUser) activity;
    }

    private void callState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        WebAPIClient.getInstance(getActivity()).get_state(getStateRequest, new Callback<GetStateResponse>() {
            @Override
            public void onResponse(Call<GetStateResponse> call, Response<GetStateResponse> response) {
                rlLoading.setVisibility(View.GONE);
                GetStateResponse getStateResponse = response.body();
                if (getStateResponse.getFlag().equals("true")) {
                    listState.clear();
                    listState.addAll(getStateResponse.getData());
                    if (!Prefs.getString(PrefsKeys.State, "").isEmpty())
                        getStateId();

                } else if (getStateResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetStateResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                t.printStackTrace();
             //   Toast.makeText(getActivity(), "state_err" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callCity() {
        rlLoading.setVisibility(View.VISIBLE);
        GetCityRequest getCityRequest = new GetCityRequest();
        getCityRequest.setStateId(STATE_ID);
        WebAPIClient.getInstance(getActivity()).get_city(getCityRequest, new Callback<GetCityResponse>() {
            @Override
            public void onResponse(Call<GetCityResponse> call, Response<GetCityResponse> response) {
                rlLoading.setVisibility(View.GONE);
                GetCityResponse getCityResponse = response.body();
                listCity.clear();
                if (getCityResponse.getFlag().equals("true")) {
                    listCity.addAll(getCityResponse.getData());
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
                    tmpData.add(listState.get(i).getStateCode());
                }
            } else {
                for (int i = 0, count = listState.size(); i < count; i++) {
                    tmpData.add(listState.get(i).getStateCode());
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
                    tmpData.add(listCity.get(i).getCityCode());
                }
            } else {
                for (int i = 0, count = listCity.size(); i < count; i++) {
                    tmpData.add(listCity.get(i).getCityCode());
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
                    txtCity.setText(listCity.get(which).getCityCode());
                    txtCity.setError(null);
                    CITY_ID = listCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.City, strName);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void callSearchTawVehicle() {
        rlLoading.setVisibility(View.VISIBLE);
        SearchTawVehicleRequest searchTawVehicleRequest = new SearchTawVehicleRequest();
        searchTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        searchTawVehicleRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE, ""));
        searchTawVehicleRequest.setVehicleCityId(CITY_ID);
        searchTawVehicleRequest.setVehicleStateId(STATE_ID);
        searchTawVehicleRequest.setVehicleNo(txtVehicleNumber.getText().toString() + "");
        //searchTawVehicleRequest.setVehicleNo("MJ");
        searchTawVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString() + "");
        //searchTawVehicleRequest.setVehicleSeriesNo("8877");
        WebAPIClient.getInstance(getActivity()).search_taw_vehicle(searchTawVehicleRequest, new Callback<SearchTawVehicleResponse>() {
            @Override
            public void onResponse(Call<SearchTawVehicleResponse> call, Response<SearchTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                SearchTawVehicleResponse searchTawVehicleResponse = response.body();
                arrPoliceStation.clear();
                if (searchTawVehicleResponse.getFlag().equals("true")) {
                    Toast.makeText(getActivity(), "Vehicle found!", Toast.LENGTH_SHORT).show();
                    SearchTawVehicleData searchTawVehicleData = searchTawVehicleResponse.getData();

                    btnNearestPoliceStation.setVisibility(View.GONE);
                    lytFound.setVisibility(View.VISIBLE);
                    lblVehicleNumber.setText(searchTawVehicleData.getVehicleNo() + "");
                    lblAddress.setText(searchTawVehicleData.getTawAddress() + "");
                    lblSeriesNumber.setText(searchTawVehicleData.getVehicleSeriesNo() + "");
                    lblTime.setText(searchTawVehicleData.getAddedOn() + "");

                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
                    lytFound.setVisibility(View.GONE);
                    btnNearestPoliceStation.setVisibility(View.VISIBLE);

                }

                // isLocationEnabled();

                //callGetNearestPoliceStations();
            }

            @Override
            public void onFailure(Call<SearchTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }


    private void gotoFragMap() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        FragMap fragMap = new FragMap();
        // fragMap.setArrPoliceStations(getNearPoliceStationDatas);
        ft.addToBackStack(FragMap.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragMap, FragMap.class.getName());
        ft.commit();
    }

    private void gotoFragPoliceStations() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        FragNearestPoliceStations fragment = new FragNearestPoliceStations();
        ft.addToBackStack(FragNearestPoliceStations.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragment, FragNearestPoliceStations.class.getName());
        ft.commit();
    }

    private void isLocationEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getActivity().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getActivity().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub


                }
            });
            dialog.show();
        } else gotoFragPoliceStations();
    }

    private void validateForm() {
        boolean ALL_IS_WELL = true;

        String vehicle_num = txtVehicleNumber.getText().toString().trim();
        String seriesNumber = txtSeriesNumber.getText().toString().trim();

        if (STATE_ID.equals("")) {
            txtState.setError("*");
            ALL_IS_WELL = false;
        } else txtState.setError(null);

        if (CITY_ID.equals("")) {
            txtCity.setError("*");
            ALL_IS_WELL = false;
        } else txtCity.setError(null);

        if (vehicle_num.equals("")) {
            txtVehicleNumber.setError("*");
            ALL_IS_WELL = false;
        } else txtVehicleNumber.setError(null);

        if (seriesNumber.equals("")) {
            txtSeriesNumber.setError("*");
            ALL_IS_WELL = false;
        } else txtSeriesNumber.setError(null);

        if (ALL_IS_WELL)
            callSearchTawVehicle();

    }

    @Override
    public void onClick(View v) {
        if (v == txtCity) {
            openCityDialog();
        } else if (v == txtState) {
            openStateDilaog();
        } else if (v == btnSearch) {

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            validateForm();
        } else if (v == btnNearestPoliceStation) {
            isLocationEnabled();
        }
    }
}
