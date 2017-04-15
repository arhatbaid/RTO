package rto.example.com.rto.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.adapters.AdapterVehicle;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationData;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.webhelper.WebAPIClient;

import static rto.example.com.rto.R.id.rlLoading;

/**
 * Created by iRoid8 on 7/23/2016.
 */
public class FragMap extends Fragment implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnInfoWindowClickListener {

     private ActHomeUser root;

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    private GoogleApiClient googleApiClient;
    private Location location;
    private LatLng latLng;
    private GoogleMap googleMap;
    private Marker marker;
    private double currLat, currLng;
    private boolean isMapClicked = false;
    private LatLng sourcelatlng, destLatlng;

    private ArrayList<GetNearPoliceStationData> arrPoliceStations;

    public void setArrPoliceStations(ArrayList<GetNearPoliceStationData> arrPoliceStations) {
        this.arrPoliceStations = arrPoliceStations;
    }

    private SupportMapFragment map;

    private String eventName, eventMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_map, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private void retriveEvents() {
        for (int i = 0; i < arrPoliceStations.size(); i++){

            double lat = Double.parseDouble(arrPoliceStations.get(i).getLatitude());
            double lng = Double.parseDouble(arrPoliceStations.get(i).getLatitude());
            drawMarker(new LatLng(lat,lng),  eventMsg, eventName);}
    }

    private void drawMarker(LatLng point,  String msg, String eventName) {

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .title(msg).snippet(eventName));

    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
               .addApi(LocationServices.API)
                .build();
    }


    private void animateCamara() {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(currLat, currLng))
                .zoom(15)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.getUiSettings().setZoomGesturesEnabled(true);

    }

    public void backPressed() {

        int childCount = getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = getChildFragmentManager();
            // removing the child Fragment from stack
            childFragmentManager.popBackStackImmediate();
        }
    }

    protected void stopLocationUpdates() {
       LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, createAndGetLocationRequest(), this);
    }


    // commented by me
    protected LocationRequest createAndGetLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    @Override
    public void onResume() {
        super.onResume();
        buildGoogleApiClient();
        if (AppHelper.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, getActivity()))
            googleApiClient.connect();
        else
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
        if (map == null)
            map = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.mapContainer, map).commit();
        map.getMapAsync(this);
    }

    @Override
    public void onPause() {
        if (map != null) {
            getChildFragmentManager().beginTransaction().remove(map).commit();
        }
        googleApiClient.disconnect();
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActHomeUser) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        backPressed();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
         startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            AppHelper.showAlertDialog(getActivity(), "Please Make yor location enabled", getString(R.string.alert));
            return;
        } else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (mLastLocation != null) {
                currLat = mLastLocation.getLatitude();
                currLng = mLastLocation.getLongitude();



            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Granted", " location permission granted");
                    googleApiClient.connect();
                } else {

                    Log.e("denied", "location permission denied");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AppHelper.showAlertDialog(getActivity(), "Location permission is required for this app.", getActivity().getString(R.string.alert));
                    } else {
                        AppHelper.showAlertDialog(getActivity(), "Go to settings and enable permissions", getActivity().getString(R.string.alert));
                    }
                }
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("status", "Event services suspended. Please reconnect.");
    }

    @Override
    public void onMapReady(GoogleMap gmap) {

        googleMap = gmap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (AppHelper.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, getActivity()))
            googleApiClient.connect();
        else
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
        googleMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapLoaded() {
        if (AppHelper.isNetConnected(getActivity())) {
            retriveEvents();

            animateCamara();
        } else
            AppHelper.showAlertDialog(getActivity(), getString(R.string.no_internet), getString(R.string.alert));

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        boolean isEventTrue = false;
//        for (int i = 0, arraySize = arrEventMap.size(); i < arraySize; i++) {
//            if (arrEventMap.get(i).getEventMessage().equals(marker.getTitle())) {
//                isEventTrue = arrEventMap.get(i).getEventStatus();
//                break;
//            }
//        }

//        if (isEventTrue) {
//            Bundle b = new Bundle();
//            b.putString("eventName", marker.getSnippet());
//            b.putString("eventMessage", marker.getTitle());
//            Intent i = new Intent(getActivity(), ActPlaceDetails.class);
//            i.putExtra("bundle", b);
//            startActivity(i);
//        }
    }
}
