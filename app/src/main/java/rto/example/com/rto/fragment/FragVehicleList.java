package rto.example.com.rto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rto.example.com.rto.R;
import rto.example.com.rto.adapters.AdapterVehicle;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleData;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

public class FragVehicleList extends Fragment implements
        View.OnClickListener, AdapterVehicle.DeleteVehicleListener {

    private RecyclerView recyclerVehicle;
    private Button btnAdd;
    private AdapterVehicle adapterVehicle;
    private RelativeLayout rlLoading;

    private ArrayList<GetVehicleData> arrVehicle = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_vehicle_list, container, false);
        findViews(view);
        callGetVehicleList();
        return view;
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

    private void callGetVehicleList() {
        rlLoading.setVisibility(View.VISIBLE);
        GetVehicleRequest getVehicleRequest = new GetVehicleRequest();
        getVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        getVehicleRequest.setUserType("3");
        WebAPIClient.getInstance(getActivity()).get_user_vehicle(getVehicleRequest, new Callback<GetVehicleResponse>() {
            @Override
            public void onResponse(Call<GetVehicleResponse> call, Response<GetVehicleResponse> response) {
                rlLoading.setVisibility(View.GONE);

                GetVehicleResponse getVehicleResponse = response.body();
                arrVehicle.clear();
                if (getVehicleResponse.getFlag().equals("true")) {

                    arrVehicle.addAll(getVehicleResponse.getData());
                    adapterVehicle = new AdapterVehicle(arrVehicle, getActivity(), FragVehicleList.this);
                    recyclerVehicle.setAdapter(adapterVehicle);
                }
            }

            @Override
            public void onFailure(Call<GetVehicleResponse> call, Throwable t) {
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
        ft.replace(R.id.fragContainer, fragEditRegisterVehicle, FragEditRegisterVehicle.class.getName());
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if (view == btnAdd) {
            gotoFragDetails();
        }
    }

    @Override
    public void viewDeleted(int pos) {
        arrVehicle.remove(pos);
        adapterVehicle.notifyDataSetChanged();
    }
}
