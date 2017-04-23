package rto.example.com.rto.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import rto.example.com.rto.activity.ActHomeOfficer;
import rto.example.com.rto.adapters.AdapterTawVehicles;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleRequest;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleResponse;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleData;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleRequest;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.Constants;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragTawVehicleList extends Fragment implements
        View.OnClickListener, AdapterTawVehicles.OnDispatchPressed {


    private ActHomeOfficer root;


    private RecyclerView recyclerVehicle;
    private Button btnAdd;
    private AdapterTawVehicles adapterTawVehicle;
    private RelativeLayout rlLoading;

    private ArrayList<GetOfficerTawVehicleData> arrVehicle = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_list_taw_vehicle, container, false);
        findViews(view);
        root.setActTitle("Taw Vehicles");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        callGetVehicleList();
    }

    private void findViews(View view) {
        recyclerVehicle = (RecyclerView) view.findViewById(R.id.recyclerVehicle);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        rlLoading = (RelativeLayout) view.findViewById(R.id.rlLoading);

        recyclerVehicle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerVehicle.setLayoutManager(llm);

        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (ActHomeOfficer) activity;

    }


    private void callGetVehicleList() {
        rlLoading.setVisibility(View.VISIBLE);
        GetOfficerTawVehicleRequest getOfficerTawVehicleRequest = new GetOfficerTawVehicleRequest();
        getOfficerTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getOfficerTawVehicleRequest.setUserType(Constants.TYPE_OFFICER);
        WebAPIClient.getInstance(getActivity()).get_officer_taw_vehicle(getOfficerTawVehicleRequest, new Callback<GetOfficerTawVehicleResponse>() {
            @Override
            public void onResponse(Call<GetOfficerTawVehicleResponse> call, Response<GetOfficerTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                GetOfficerTawVehicleResponse getOfficerTawVehicleResponse = response.body();
                arrVehicle.clear();
                if (getOfficerTawVehicleResponse.getFlag().equals("true")) {

                    arrVehicle.addAll(getOfficerTawVehicleResponse.getData());
                    adapterTawVehicle = new AdapterTawVehicles(arrVehicle, getActivity(), FragTawVehicleList.this);
                    recyclerVehicle.setAdapter(adapterTawVehicle);
                }
            }

            @Override
            public void onFailure(Call<GetOfficerTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void gotoFragDetails() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        FragEditRegisterVehicle fragEditRegisterVehicle = new FragEditRegisterVehicle();
        fragEditRegisterVehicle.setIsRegister(true); // Register
        ft.addToBackStack(FragEditRegisterVehicle.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainerOfficer, fragEditRegisterVehicle, FragEditRegisterVehicle.class.getName());
        ft.commit();
    }

    private void callDispatchVehicle(int pos, String amount, String chllanNumber) {
        rlLoading.setVisibility(View.VISIBLE);

        DispatchTawVehicleRequest dispatchTawVehicleRequest = new DispatchTawVehicleRequest();
        dispatchTawVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        dispatchTawVehicleRequest.setUserType(Constants.TYPE_OFFICER);
        dispatchTawVehicleRequest.setAmount(amount);
        dispatchTawVehicleRequest.setChallanNumber(chllanNumber);

        Log.e("taw_id", arrVehicle.get(pos).getTawId());
        dispatchTawVehicleRequest.setTawIds(arrVehicle.get(pos).getTawId());

        WebAPIClient.getInstance(getActivity()).dispatch_taw_vehicle(dispatchTawVehicleRequest, new Callback<DispatchTawVehicleResponse>() {
            @Override
            public void onResponse(Call<DispatchTawVehicleResponse> call, Response<DispatchTawVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                DispatchTawVehicleResponse addTawVehicleResponse = response.body();
                if (addTawVehicleResponse.getFlag().equals("true")) {
                    Toast.makeText(getActivity(), "Vehicle dispatched successfully!", Toast.LENGTH_SHORT).show();
                    onStart();
                } else if (addTawVehicleResponse.getFlag().equals("false")) {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DispatchTawVehicleResponse> call, Throwable t) {
                rlLoading.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChallanDialog(final int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText txtAmount = (EditText) dialogView.findViewById(R.id.txtAmount);
        final EditText txtChallan = (EditText) dialogView.findViewById(R.id.txtChallanNumber);

        dialogBuilder.setPositiveButton("Dispatch", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                boolean amount = AppHelper.CheckEditText(txtAmount);
                boolean challan_number = AppHelper.CheckEditText(txtChallan);
                if (amount && challan_number) {
                    dialog.dismiss();
                    callDispatchVehicle(pos, txtAmount.getText().toString(), txtChallan.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    @Override
    public void onClick(View view) {
        if (view == btnAdd) {
            gotoFragDetails();
        }
    }


    @Override
    public void onDispatchClick(int pos) {
        showChallanDialog(pos);
    }
}

