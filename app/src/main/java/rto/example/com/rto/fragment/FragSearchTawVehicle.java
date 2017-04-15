package rto.example.com.rto.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import rto.example.com.rto.adapters.AdapterVehicle;
import rto.example.com.rto.frameworks.city.GetCityData;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationData;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationRequest;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleData;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleRequest;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleResponse;
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

import static rto.example.com.rto.R.id.rlLoading;

/**
 * Created by ridz1 on 14/04/2017.
 */

public class FragSearchTawVehicle extends Fragment implements View.OnClickListener {

    private EditText txtState;
    private EditText txtCity;
    private EditText txtSeriesNumber;
    private EditText txtVehicleNumber;
    private RelativeLayout rlLoading;
    private Button btnSearch;

    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();
    private ArrayList<GetNearPoliceStationData> arrPoliceStation = new ArrayList<>();

    private String STATE_ID = "";
    private String CITY_ID = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_taw_vehicle, container, false);
        findViews(view);
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
        btnSearch.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtCity.setOnClickListener(this);
    }

    private void callState() {
        rlLoading.setVisibility(View.VISIBLE);
        GetStateRequest getStateRequest = new GetStateRequest();
        getStateRequest.setUserId(Prefs.getString(PrefsKeys.USERID,""));
        getStateRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE,""));
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
        getCityRequest.setUserId(Prefs.getString(PrefsKeys.USERID,""));
        getCityRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE,""));
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

    private void callSearchTawVehicle() {
        rlLoading.setVisibility(View.VISIBLE);
        SearchTawVehicleRequest searchTawVehicleRequest = new SearchTawVehicleRequest();
        searchTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        searchTawVehicleRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE, ""));
        searchTawVehicleRequest.setVehicleCityId(CITY_ID);
        searchTawVehicleRequest.setVehicleStateId(STATE_ID);
        //searchTawVehicleRequest.setVehicleNo(txtVehicleNumber.getText().toString()+"");
        searchTawVehicleRequest.setVehicleNo("MJ");
       // searchTawVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString()+"");
        searchTawVehicleRequest.setVehicleSeriesNo("8877");
        WebAPIClient.getInstance(getActivity()).search_taw_vehicle(searchTawVehicleRequest, new Callback<SearchTawVehicleResponse>() {
            @Override
            public void onResponse(Call<SearchTawVehicleResponse> call, Response<SearchTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                SearchTawVehicleResponse searchTawVehicleResponse = response.body();
                arrPoliceStation.clear();
                if (searchTawVehicleResponse.getFlag().equals("true")) {

                } else
                    callGetNearestPoliceStations();
            }

            @Override
            public void onFailure(Call<SearchTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void callGetNearestPoliceStations() {
        rlLoading.setVisibility(View.VISIBLE);
        GetNearPoliceStationRequest getNearPoliceStationRequest = new GetNearPoliceStationRequest();
        getNearPoliceStationRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getNearPoliceStationRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE, ""));
        getNearPoliceStationRequest.setLatitude("27.12345");
        getNearPoliceStationRequest.setLongitude("-72.1556");
        WebAPIClient.getInstance(getActivity()).search_nearest_police_station(getNearPoliceStationRequest, new Callback<GetNearPoliceStationResponse>() {
            @Override
            public void onResponse(Call<GetNearPoliceStationResponse> call, Response<GetNearPoliceStationResponse> response) {
                rlLoading.setVisibility(View.GONE);

                GetNearPoliceStationResponse getNearPoliceStationResponse = response.body();
                arrPoliceStation.clear();
                if (getNearPoliceStationResponse.getFlag().equals("true")) {

                    arrPoliceStation.addAll(getNearPoliceStationResponse.getData());
                    gotoFragDetails(arrPoliceStation);

                }
            }

            @Override
            public void onFailure(Call<GetNearPoliceStationResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void gotoFragDetails(ArrayList<GetNearPoliceStationData> getNearPoliceStationDatas) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        FragMap fragMap = new FragMap();
        fragMap.setArrPoliceStations(getNearPoliceStationDatas);
        ft.addToBackStack(FragMap.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragMap, FragMap.class.getName());
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == txtCity) {
            openCityDialog();
        } else if (v == txtState) {
            openStateDilaog();
        }else if (v==btnSearch){
            callSearchTawVehicle();
        }
    }
}
