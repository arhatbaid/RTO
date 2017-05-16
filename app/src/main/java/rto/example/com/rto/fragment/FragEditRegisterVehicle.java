package rto.example.com.rto.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import rto.example.com.rto.frameworks.state.GetStateData;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragEditRegisterVehicle extends Fragment implements View.OnClickListener {

    private ActHomeUser root;


    private static final int REQUEST_IMAGE = 100;
    private static final int STORAGE=1;
    private String ANDROID_DATA_DIR;
    private static File destination;

    private EditText txtVehicleName;
    private EditText txtState;
    private EditText txtCity;
    private EditText txtVehicleType;
    private EditText txtSeriesNumber;
    private EditText txtCityCode;
    private EditText txtVehicleNumber;
    private EditText txtVehicleBuyDate;
    private EditText txtPUCNumber;
    private EditText txtPUCPurchaseDate;
    private Button btnAddVehicle;
    private RelativeLayout rlLoading;
    private ImageView imgCamera;

    ArrayList<String> tmpData = new ArrayList<>();
    private GetVehicleData getVehicleData = null;
    private boolean isRegister; // if flase then edit if true then register
    private String STATE_ID = "", CITY_ID = "";
    private ArrayList<GetStateData> listState = new ArrayList<>();
    private ArrayList<GetCityData> listCity = new ArrayList<>();

    public void setGetVehicleData(GetVehicleData getVehicleData) {
        this.getVehicleData = getVehicleData;
    }

    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.frag_register_vehicle, container, false);
        findViews(view);

        if (!isRegister) {
            root.setActTitle("Update Vehicle");
            btnAddVehicle.setText("Update Vehicle");
            setContent();
        }else
            root.setActTitle("Add Vehicle");
        callState();
        if (!isRegister) {
            callCity(true);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActHomeUser) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        root.setActTitle("Vehicles");
    }

    private void findViews(View view) {
        txtVehicleName = (EditText) view.findViewById(R.id.txtVehicleName);
        txtState = (EditText) view.findViewById(R.id.txtState);
        txtCity = (EditText) view.findViewById(R.id.txtCity);
        txtVehicleType = (EditText) view.findViewById(R.id.txtVehicleType);
        txtSeriesNumber = (EditText) view.findViewById(R.id.txtSeriesNumber);
        txtCityCode = (EditText) view.findViewById(R.id.txtCityCode);
        txtVehicleNumber = (EditText) view.findViewById(R.id.txtVehicleNumber);
        txtVehicleBuyDate = (EditText) view.findViewById(R.id.txtVehicleBuyDate);
        txtPUCNumber = (EditText) view.findViewById(R.id.txtPUCNumber);
        txtPUCPurchaseDate = (EditText) view.findViewById(R.id.txtPUCPurchaseDate);
        imgCamera = (ImageView) view.findViewById(R.id.imgCamera);

        txtSeriesNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnAddVehicle = (Button) view.findViewById(R.id.btnAddVehicle);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        btnAddVehicle.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
        txtState.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        txtVehicleType.setOnClickListener(this);
        txtVehicleBuyDate.setOnClickListener(this);
        txtPUCPurchaseDate.setOnClickListener(this);


        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        txtVehicleBuyDate.setText(fDate);


        tmpData.add("Car");
        tmpData.add("Bike");
    }

    private void setContent() {
        String name = getVehicleData.getVehicleName();
        String number = getVehicleData.getVehicleNo();
        String number_plate = getVehicleData.getVehicleNumberPlate();
        String series_no = getVehicleData.getVehicleSeriesNo();
        String type = getVehicleData.getVehicleType();
        String city_code = getVehicleData.getCityCode();
        String state_code = getVehicleData.getStateCode();
        String PucNumber = getVehicleData.getPucNumber();
        String purchase_date = getVehicleData.getPucPurchaseDate();
        String buy_date = getVehicleData.getBuyingDate();
        STATE_ID = getVehicleData.getStateId();
        CITY_ID = getVehicleData.getCityId();


        if (AppHelper.isValidString(name))
            txtVehicleName.setText(name);

        if (AppHelper.isValidString(PucNumber))
            txtPUCNumber.setText(PucNumber);

        if (AppHelper.isValidString(number))
            txtVehicleNumber.setText(number);

        if (AppHelper.isValidString(number))
            txtCityCode.setText(city_code);

        if (AppHelper.isValidString(purchase_date))
            txtPUCPurchaseDate.setText(purchase_date.split(" ")[0]);

        if (AppHelper.isValidString(buy_date))
            txtVehicleBuyDate.setText(buy_date.split(" ")[0]);

        if (AppHelper.isValidString(series_no))
            txtSeriesNumber.setText(series_no);

        if (AppHelper.isValidString(type))
            txtVehicleType.setText(type.equals("1") ? "Bike" : "Car");
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

            /*AsyncTask.execute(new Runnable() {
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
                                    Toast.makeText(getActivity(), "It was not possible to detect the licence plate.Please type manually.", Toast.LENGTH_LONG).show();
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
            });
*/        }
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
            case STORAGE:{
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



    private void callAddVehicle() {
        rlLoading.setVisibility(View.VISIBLE);

        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
        addVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        addVehicleRequest.setUserType(Constants.TYPE_USER);
        addVehicleRequest.setVehicleName(txtVehicleName.getText().toString());
        addVehicleRequest.setStateId(STATE_ID);
        addVehicleRequest.setCityId(CITY_ID);
        addVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        addVehicleRequest.setVehicleType(txtVehicleType.getText().toString().equals("Car") ? "2" : "1");
        addVehicleRequest.setVehicleNo(txtVehicleNumber.getText().toString());
        addVehicleRequest.setBuyingDate(txtVehicleBuyDate.getText().toString());
        addVehicleRequest.setPucNumber(txtPUCNumber.getText().toString());
        addVehicleRequest.setPucPurchaseDate(txtPUCPurchaseDate.getText().toString());


        WebAPIClient.getInstance(getActivity()).add_vehicle(addVehicleRequest, new Callback<AddVehicleResponse>() {
            @Override
            public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                AddVehicleResponse addVehicleResponse = response.body();
                if (addVehicleResponse.getFlag().equals("true")) {
                    rlLoading.setVisibility(View.GONE);
                    getActivity().getSupportFragmentManager().popBackStack();
                } else if (addVehicleResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callEditVehicle() {
        EditVehicleRequest editVehicleRequest = new EditVehicleRequest();
        editVehicleRequest.setVehicleId(getVehicleData.getVehicleId());
        editVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        editVehicleRequest.setUserType(Constants.TYPE_USER);
        editVehicleRequest.setVehicleName(txtVehicleName.getText().toString());
        editVehicleRequest.setStateId(STATE_ID);
        editVehicleRequest.setCityId(CITY_ID);
        editVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        editVehicleRequest.setVehicleType(txtVehicleType.getText().toString().equals("Car") ? "2" : "1");
        editVehicleRequest.setVehicleNo(txtVehicleNumber.getText().toString());
        editVehicleRequest.setBuyingDate(txtVehicleBuyDate.getText().toString());
        editVehicleRequest.setPucNumber(txtPUCNumber.getText().toString());
        editVehicleRequest.setPucPurchaseDate(txtPUCPurchaseDate.getText().toString());
        rlLoading.setVisibility(View.VISIBLE);
        WebAPIClient.getInstance(getActivity()).edit_vehicle(editVehicleRequest, new Callback<EditVehicleResponse>() {
            @Override
            public void onResponse(Call<EditVehicleResponse> call, Response<EditVehicleResponse> response) {
                EditVehicleResponse getEditVehicleResponse = response.body();
                if (getEditVehicleResponse.getFlag().equals("true")) {
                    rlLoading.setVisibility(View.GONE);
                    getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(root, "Vehicle updated successfully!", Toast.LENGTH_SHORT).show();

                } else if (getEditVehicleResponse.getFlag().equals("false")) {
                    rlLoading.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddVehicle) {
            if (isValid()) {
                if (isRegister) {
                    callAddVehicle();
                } else {
                    callEditVehicle();
                }
            }
        } else if (v == txtVehicleType) {
            vehicleTypeDialog();
        } else if (v == txtState) {
            openStateDilaog();
        } else if (v == txtCity) {
            openCityDialog();
        } else if (v == txtVehicleBuyDate) {
            showDatePickerDialog(true);
        } else if (v == txtPUCPurchaseDate) {
            showDatePickerDialog(false);
        } else if (v == imgCamera) {
            checkPermission();
        }
    }

    private boolean isValid() {
        if (txtVehicleName.getText().toString().trim().isEmpty()) {
            txtVehicleName.setError("*");
            return false;
        } else {
            txtVehicleName.setError(null);
        }

        if (txtVehicleType.getText().toString().trim().isEmpty()) {
            txtVehicleType.setError("*");
            return false;
        } else {
            txtVehicleType.setError(null);
        }

        if (txtSeriesNumber.getText().toString().trim().isEmpty()) {
            txtSeriesNumber.setError("*");
            return false;
        } else {
            txtSeriesNumber.setError(null);
        }

       /* if (txtCityCode.getText().toString().trim().isEmpty()) {
            txtCityCode.setError("*");
            return false;
        } else {
            txtCityCode.setError(null);
        }*/

        if (txtVehicleNumber.getText().toString().trim().isEmpty()) {
            txtVehicleNumber.setError("*");
            return false;
        } else {
            txtVehicleNumber.setError(null);
        }

        if (txtVehicleBuyDate.getText().toString().trim().isEmpty()) {
            txtVehicleBuyDate.setError("*");
            return false;
        } else {
            txtVehicleBuyDate.setError(null);
        }

       /* if (txtPUCNumber.getText().toString().trim().isEmpty()) {
            txtPUCNumber.setError("*");
            return false;
        } else {
            txtPUCNumber.setError(null);
        }

        if (txtPUCPurchaseDate.getText().toString().trim().isEmpty()) {
            txtPUCPurchaseDate.setError("*");
            return false;
        } else {
            txtPUCPurchaseDate.setError(null);
        }*/
        return true;
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
                    if (STATE_ID.isEmpty())
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

    private void getStateId() {
        //If no state is selected
        if (listState.size() > 0) {
            for (int i = 0, count = listState.size(); i < count; i++) {
                if (STATE_ID.equalsIgnoreCase(listState.get(i).getStateId())) {
                    txtState.setText(listState.get(i).getStateCode());
                    break;
                }
            }
        }
    }

    private void openStateDilaog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listState.size() > 0) {
            for (int i = 0, count = listState.size(); i < count; i++) {
                tmpData.add(listState.get(i).getStateName());
            }
        }
        placeDialogue(Constants.STATE, tmpData, selectedOption);
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

                    txtState.setText(listState.get(which).getStateCode());
                    txtCity.setText("");
                    callCity(false);
                    txtState.setError(null);
                    txtCity.setError(null);
                } else if (type.equalsIgnoreCase(Constants.CITY)) {
                    txtCity.setText(listCity.get(which).getCityCode());
                    txtCity.setError(null);
                    CITY_ID = listCity.get(which).getCityId();
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void callCity(final boolean predefined) {
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
                    if (predefined) {
                       getCityId();
                    } else {

                    }
                    rlLoading.setVisibility(View.GONE);
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

    private void getCityId() {
        //If no state is selected
        if (listCity.size() > 0) {
            for (int i = 0, count = listCity.size(); i < count; i++) {
                if (CITY_ID.equalsIgnoreCase(listCity.get(i).getCityId())) {
                    txtCity.setText(listCity.get(i).getCityCode());
                    break;
                }
            }
        }
    }

    private void openCityDialog() {
        int selectedOption = -1;
        ArrayList<String> tmpData = new ArrayList<>();
        if (listCity.size() > 0) {
            for (int i = 0, count = listCity.size(); i < count; i++) {
                tmpData.add(listCity.get(i).getCityName());
            }
        }
        placeDialogue(Constants.CITY, tmpData, selectedOption);
    }

    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private boolean isBuyingDate = false;

        public void isBuyingDate(boolean isBuyingDate) {
            this.isBuyingDate = isBuyingDate;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (isBuyingDate) {
                txtVehicleBuyDate.setText(year + "-" + month + "-" + day);
            } else {
                txtPUCPurchaseDate.setText(year + "-" + month + "-" + day);
            }
        }
    }

    public void showDatePickerDialog(boolean isBuyingDate) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.isBuyingDate(isBuyingDate);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}
