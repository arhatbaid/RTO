package rto.example.com.rto.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.adapters.AdapterDispatchedTawVehicles;
import rto.example.com.rto.adapters.AdapterPoliceStations;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationData;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationRequest;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

/**
 * Created by ridz1 on 10/05/2017.
 */

public class FragNearestPoliceStations extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = FragNearestPoliceStations.class.getName();
    private ArrayList<GetNearPoliceStationData> arrPoliceStations = new ArrayList<>();
    private AdapterPoliceStations adapterPoliceStations = null;
    private RelativeLayout rlLoading = null;
    private RecyclerView recyclerPoliceStation;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private LatLng latLng;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_police_station, container, false);


        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        findView(view);
        return view;
    }

    private void findView(View view) {
        recyclerPoliceStation = (RecyclerView) view.findViewById(R.id.recyclerStations);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        recyclerPoliceStation.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapterPoliceStations = new AdapterPoliceStations(getActivity());
        recyclerPoliceStation.setLayoutManager(llm);
        recyclerPoliceStation.setAdapter(adapterPoliceStations);

    }

    private void callGetNearestPoliceStations(String lat, String lng) {
        rlLoading.setVisibility(View.VISIBLE);
        GetNearPoliceStationRequest getNearPoliceStationRequest = new GetNearPoliceStationRequest();
        getNearPoliceStationRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getNearPoliceStationRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE, ""));
        getNearPoliceStationRequest.setLatitude(lat);
        getNearPoliceStationRequest.setLongitude(lng);
        WebAPIClient.getInstance(getActivity()).search_nearest_police_station(getNearPoliceStationRequest, new Callback<GetNearPoliceStationResponse>() {
            @Override
            public void onResponse(Call<GetNearPoliceStationResponse> call, Response<GetNearPoliceStationResponse> response) {
                rlLoading.setVisibility(View.GONE);

                GetNearPoliceStationResponse getNearPoliceStationResponse = response.body();
                arrPoliceStations.clear();
                String flag = getNearPoliceStationResponse.getFlag();
                Log.e("flag", flag);
                if (getNearPoliceStationResponse.getFlag().equals("true")) {

                    arrPoliceStations.addAll(getNearPoliceStationResponse.getData());
                    adapterPoliceStations.addAll(arrPoliceStations);
                    //gotoFragDetails(arrPoliceStation);

                } else {
                    Toast.makeText(getActivity(), "There is no any police station near to your location", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetNearPoliceStationResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getActivity(), "Please enable your GPS", Toast.LENGTH_SHORT).show();
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            double currLat = mLastLocation.getLatitude();
            double currLng = mLastLocation.getLongitude();

            if (currLat != 0 && currLng != 0)
                callGetNearestPoliceStations(currLat + "", currLng + "");
            else
                Toast.makeText(getActivity(), "Please enable your GPS", Toast.LENGTH_SHORT).show();
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            callGetNearestPoliceStations(lat, lng);
//            tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
//                    "Latitude: " + lat + "\n" +
//                    "Longitude: " + lng + "\n" +
//                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
//                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient.isConnected())
            startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
Log.e("locationChanged","locationChanged");
        if (location != null) {
            double currLat = location.getLatitude();
            double currLng = location.getLongitude();

            if (currLat != 0 && currLng != 0)
                callGetNearestPoliceStations(currLat + "", currLng + "");
            else
                Toast.makeText(getActivity(), "Please enable your GPS", Toast.LENGTH_SHORT).show();
        }
    }
}
