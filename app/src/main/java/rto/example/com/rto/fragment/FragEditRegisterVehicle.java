package rto.example.com.rto.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleRequest;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleData;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragEditRegisterVehicle extends Fragment implements View.OnClickListener {

    private EditText txtVehicleName;
    private EditText txtVehicleType;
    private EditText txtSeriesNumber;
    private EditText txtCityCode;
    private EditText txtVehicleNumber;
    private EditText txtVehicleBuyDate;
    private EditText txtPUCNumber;
    private EditText txtPUCPurchaseDate;
    private Button btnAddVehicle;
    private RelativeLayout rlLoading;

    private GetVehicleData getVehicleData = null;
    private boolean isRegister; // if flase then edit if true then register

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
            btnAddVehicle.setText("Update Vehicle");
            setContent();
        }
        return view;
    }

    private void findViews(View view) {
        txtVehicleName = (EditText) view.findViewById(R.id.txtVehicleName);
        txtVehicleType = (EditText) view.findViewById(R.id.txtVehicleType);
        txtSeriesNumber = (EditText) view.findViewById(R.id.txtSeriesNumber);
        txtCityCode = (EditText) view.findViewById(R.id.txtCityCode);
        txtVehicleNumber = (EditText) view.findViewById(R.id.txtVehicleNumber);
        txtVehicleBuyDate = (EditText) view.findViewById(R.id.txtVehicleBuyDate);
        txtPUCNumber = (EditText) view.findViewById(R.id.txtPUCNumber);
        txtPUCPurchaseDate = (EditText) view.findViewById(R.id.txtPUCPurchaseDate);

        btnAddVehicle = (Button) view.findViewById(R.id.btnAddVehicle);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        btnAddVehicle.setOnClickListener(this);
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
            txtVehicleType.setText(type);
    }

    private void callAddVehicle() {
        rlLoading.setVisibility(View.VISIBLE);

        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
        addVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        addVehicleRequest.setUserType("3");
        addVehicleRequest.setVehicleName(txtVehicleName.getText().toString());
        addVehicleRequest.setStateId(getVehicleData.getStateId());
        addVehicleRequest.setCityId(getVehicleData.getCityId());
        addVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        //  addVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
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
        editVehicleRequest.setUserType("3");
        editVehicleRequest.setVehicleName(txtVehicleName.getText().toString());
        editVehicleRequest.setStateId(getVehicleData.getStateId());
        editVehicleRequest.setCityId(getVehicleData.getCityId());
        editVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
        //  editVehicleRequest.setVehicleSeriesNo(txtSeriesNumber.getText().toString());
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

        if (txtPUCNumber.getText().toString().trim().isEmpty()) {
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
        }
        return true;
    }

    private void vehicleTypeDialog(final String type, ArrayList<String> tmpData, int selectedOption) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        builderSingle.setTitle("Select vehicle type :-");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, tmpData);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builderSingle.setSingleChoiceItems(arrayAdapter, selectedOption,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

}
