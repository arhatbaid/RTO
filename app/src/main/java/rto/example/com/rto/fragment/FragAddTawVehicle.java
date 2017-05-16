package rto.example.com.rto.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeOfficer;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleRequest;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleResponse;
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

    private static final int REQUEST_IMAGE = 100;
    private static final int STORAGE = 1;
    private String ANDROID_DATA_DIR;
    private static File destination;

    private ImageView imgCamara;
    private EditText txtVehicleName;
    private EditText txtVehicleState;
    private EditText txtVehicleCity;
    private EditText txtSeriesNumber;
    private EditText txtVehicleNumber;
    private EditText txtState;
    private EditText txtCity;
    private EditText txtAddress;
    private TextView lblCount;
    private EditText txtVehicleType;
    private LinearLayout lytTimer;
    private Button btnCancel;
    private RelativeLayout rlLoading;
    private Button btnAddVehicle;
    ArrayList<String> tmpData = new ArrayList<>();
    private String STATE_ID = "", CITY_ID = "";
    private String VEHICLE_STATE_ID = "", VEHICEL_CITY_ID = "";
    private ArrayList<GetStateData> listVehicleState = new ArrayList<>();
    private ArrayList<GetCityData> listVehicleCity = new ArrayList<>();
    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();

    private CountDownTimer countDownTimer;


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
        root = (ActHomeOfficer) activity;
    }

    private void findViews(View view) {
        imgCamara = (ImageView) view.findViewById(R.id.imgCamara);
        txtVehicleName = (EditText) view.findViewById(R.id.txtVehicleName);
        lblCount = (TextView) view.findViewById(R.id.lblCount);
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
        lytTimer = (LinearLayout) view.findViewById(R.id.lytTimer);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        txtSeriesNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        imgCamara.setOnClickListener(this);
        btnAddVehicle.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtVehicleCity.setOnClickListener(this);
        txtVehicleState.setOnClickListener(this);
        txtVehicleType.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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

        } else if (v == btnCancel) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                lytTimer.setVisibility(View.GONE);
            }
        } else if (v == imgCamara) {
            checkPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            final ProgressDialog progress = ProgressDialog.show(getActivity(), "Loading", "Parsing result...", true);
            final String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;

            // Picasso requires permission.WRITE_EXTERNAL_STORAGE
            // Picasso.with(this).load(destination).fit().centerCrop().into(imageView);
            txtVehicleNumber.setText("Processing");

          /*  AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    String result = OpenALPR.Factory.create(getActivity(), ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "", destination.getAbsolutePath(), openAlprConfFile, 10);

                    Log.d("OPEN ALPR", result);

                    try {
                        final Results results = new Gson().fromJson(result, Results.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                                    Toast.makeText(getActivity(), "It was not possible to detect the licence plate.Please type manually", Toast.LENGTH_LONG).show();
                                    txtVehicleNumber.setText("");
                                } else {
                                    txtVehicleNumber.setText("Plate: " + results.getResults().get(0).getPlate()
                                            // Trim confidence to two decimal places
                                            + " Confidence: " + String.format("%.2f", results.getResults().get(0).getConfidence()) + "%"
                                            // Convert processing time to seconds and trim to two decimal places
                                            + " Processing time: " + String.format("%.2f", ((results.getProcessingTimeMs() / 1000.0) % 60)) + " seconds");
                                }
                            }
                        });

                    } catch (JsonSyntaxException exception) {
                        final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtVehicleNumber.setText(resultsError.getMsg());
                            }
                        });
                    }

                    progress.dismiss();
                }
            });*/
        }
    }

    private void checkPermission() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(getActivity(), "Storage access needed to manage the picture.", Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(getActivity(), params, STORAGE);
        } else { // We already have permissions, so handle as normal
            takePicture();
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public void takePicture() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for WRITE_EXTERNAL_STORAGE
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // permission was granted, yay!
                    takePicture();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Storage permission is needed to analyse the picture.", Toast.LENGTH_LONG).show();
                }
            }
            default:
                break;
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
            callAddTawVehicle("1");
        }

    }

    private void callAddTawVehicle(String checkSecondTime) {
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
        addTawVehicleRequest.setCheckSecondTime(checkSecondTime);
        addTawVehicleRequest.setTawAddress(txtAddress.getText().toString());


        WebAPIClient.getInstance(getActivity()).add_taw_vehicle(addTawVehicleRequest, new Callback<AddTawVehicleResponse>() {
            @Override
            public void onResponse(Call<AddTawVehicleResponse> call, Response<AddTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                AddTawVehicleResponse addTawVehicleResponse = response.body();
                if (addTawVehicleResponse.getFlag().equals("true")) {
                    Toast.makeText(getActivity(), "Vehicle tawed successfully!", Toast.LENGTH_SHORT).show();
                    //   startTimer();
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

                    if (addTawVehicleResponse.getDeclaration().equals("DUPLICATE_ENTRY")) {

                        Toast.makeText(getActivity(), addTawVehicleResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else
                        startTimer();
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
        WebAPIClient.getInstance(getActivity()).get_state(getStateRequest, new Callback<GetStateResponse>() {
            @Override
            public void onResponse(Call<GetStateResponse> call, Response<GetStateResponse> response) {
                rlLoading.setVisibility(View.GONE);
                GetStateResponse getStateResponse = response.body();
                if (getStateResponse.getFlag().equals("true")) {
                    listVehicleState.clear();
                    listVehicleState.addAll(getStateResponse.getData());
                    if (!Prefs.getString(PrefsKeys.VehicleState, "").isEmpty())
                        getVehicleStateId();
                } else if (getStateResponse.getFlag().equals("false")) {
                }
            }

            @Override
            public void onFailure(Call<GetStateResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                //  Toast.makeText(getActivity(), "state_err" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callVehicleCity() {
        rlLoading.setVisibility(View.VISIBLE);
        GetCityRequest getCityRequest = new GetCityRequest();
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
                if (Prefs.getString(PrefsKeys.VehicleState, "").equalsIgnoreCase(listVehicleState.get(i).getStateName())) {
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
            if (Prefs.getString(PrefsKeys.VehicleState, "").isEmpty()) {
                for (int i = 0, count = listVehicleState.size(); i < count; i++) {
                    tmpData.add(listVehicleState.get(i).getStateCode());
                }
            } else {
                for (int i = 0, count = listVehicleState.size(); i < count; i++) {
                    tmpData.add(listVehicleState.get(i).getStateCode());
                    if (listVehicleState.get(i).getStateCode().equalsIgnoreCase(Prefs.getString(PrefsKeys.VehicleState, ""))) {
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
            if (Prefs.getString(PrefsKeys.VehicleCity, "").isEmpty()) {
                for (int i = 0, count = listVehicleCity.size(); i < count; i++) {
                    tmpData.add(listVehicleCity.get(i).getCityCode());
                }
            } else {
                for (int i = 0, count = listVehicleCity.size(); i < count; i++) {
                    tmpData.add(listVehicleCity.get(i).getCityCode());
                    if (listVehicleCity.get(i).getCityCode().equalsIgnoreCase(Prefs.getString(PrefsKeys.VehicleCity, ""))) {
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
                    Prefs.putString(PrefsKeys.VehicleState, strName);
                    Prefs.putString(PrefsKeys.VehicleCity, "");
                    txtVehicleCity.setText("");
                    callVehicleCity();
                    txtVehicleCity.setError(null);
                    txtVehicleCity.setError(null);
                } else if (type.equalsIgnoreCase(Constants.CITY)) {
                    txtVehicleCity.setText(listVehicleCity.get(which).getCityName());
                    txtVehicleCity.setError(null);
                    VEHICEL_CITY_ID = listVehicleCity.get(which).getCityId();
                    Prefs.putString(PrefsKeys.VehicleCity, strName);
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
                    txtCity.setText(listCity.get(which).getCityName());
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

    public void startTimer() {

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            AlertDialog dialog;

            public void onTick(long millisUntilFinished) {
                lytTimer.setVisibility(View.GONE);
                lblCount.setText("" + millisUntilFinished / 1000);
                dialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setMessage("" + millisUntilFinished / 1000)
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                countDownTimer.cancel();
                                // do nothing
                            }
                        })
                        .show();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.dimAmount = 0.7f;
            }

            public void onFinish() {
                lytTimer.setVisibility(View.GONE);
                dialog.dismiss();
                callAddTawVehicle("0");
                // lblCount.setText("done!");
            }

        }.start();

    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(getActivity())
                .setMessage("")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
