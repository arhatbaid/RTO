package rto.example.com.rto.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeOfficer;
import rto.example.com.rto.adapters.AdapterDispatchedTawVehicles;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleRequest;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleResponse;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.DispatchedTawVehicle;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.GetDispatchedTawVehicleRequest;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.GetDispatchedTawVehicleResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;


public class FragGetDispatchedTawVehicle extends Fragment {

    private ActHomeOfficer root;

    private AdapterDispatchedTawVehicles adapterDispatchedTawVehicles = null;
    private RelativeLayout rlLoading = null;
    private ListView listDispatchedVehicle = null;

    private ArrayList<DispatchedTawVehicle> dispatchedTawVehicles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_get_dispatched_taw_vehicle, container, false);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);
        listDispatchedVehicle = (ListView) view.findViewById(R.id.listDispatchedVehicle);
        root.setActTitle("Dispatched Taw Vehicles");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDispatchedVehicle();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActHomeOfficer) activity;

    }

    private void getDispatchedVehicle() {
        rlLoading.setVisibility(View.VISIBLE);
        GetDispatchedTawVehicleRequest getDispatchedTawVehicleRequest = new GetDispatchedTawVehicleRequest();
        getDispatchedTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getDispatchedTawVehicleRequest.setUserType("3");
        WebAPIClient.getInstance(getActivity()).get_dispatched_vehicle(getDispatchedTawVehicleRequest, new Callback<GetDispatchedTawVehicleResponse>() {
            @Override
            public void onResponse(Call<GetDispatchedTawVehicleResponse> call, Response<GetDispatchedTawVehicleResponse> response) {
                GetDispatchedTawVehicleResponse signinResponse = response.body();
                rlLoading.setVisibility(View.GONE);
                dispatchedTawVehicles.clear();
                if (signinResponse.getFlag().equals("true")) {
                    dispatchedTawVehicles.addAll(signinResponse.getData());
                    if (dispatchedTawVehicles.isEmpty())
                        Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    adapterDispatchedTawVehicles = new AdapterDispatchedTawVehicles(getActivity(), 0, dispatchedTawVehicles);
                    listDispatchedVehicle.setAdapter(adapterDispatchedTawVehicles);
                } else {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetDispatchedTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
