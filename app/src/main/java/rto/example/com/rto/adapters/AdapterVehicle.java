package rto.example.com.rto.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import rto.example.com.rto.fragment.FragRegisterVehicle;
import rto.example.com.rto.fragment.FragVehicleList;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleRequest;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleData;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.helper.AppHelper;
import rto.example.com.rto.helper.PrefsKeys;
import rto.example.com.rto.webhelper.WebAPIClient;

/**
 * Created by ridz1 on 13/04/2017.
 */

public class AdapterVehicle extends RecyclerView.Adapter<AdapterVehicle.ViewHolder> {

    private ArrayList<GetVehicleData> arrVehicle;
    private Context context;

    public AdapterVehicle(ArrayList<GetVehicleData> arrVehicle, Context context) {
        this.arrVehicle = arrVehicle;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if (AppHelper.isValidString(arrVehicle.get(position).getVehicleName()))
            holder.lblVehicleName.setText(arrVehicle.get(position).getVehicleName());

        if (AppHelper.isValidString(arrVehicle.get(position).getVehicleNumberPlate()))
            holder.lblVehicleNumberPlate.setText(arrVehicle.get(position).getVehicleNumberPlate());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDeleteConfirmDialog(position);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFragDetails(arrVehicle.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrVehicle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblVehicleName;
        TextView lblVehicleNumberPlate;
        ImageView imgEdit;
        ImageView imgDelete;


        public ViewHolder(View itemView) {
            super(itemView);

            lblVehicleName = (TextView) itemView.findViewById(R.id.lblVehicleName);
            lblVehicleNumberPlate = (TextView) itemView.findViewById(R.id.lblVehicleNumberPlate);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        }
    }

    private void showDeleteConfirmDialog(final int pos){
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                      //  arrVehicle.remove(pos);
                        callDeleteVehicle(arrVehicle.get(pos).getVehicleId());
                        //notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void callDeleteVehicle(String id) {
        //rlLoading.setVisibility(View.VISIBLE);
        DeleteVehicleRequest deleteVehicleRequest = new DeleteVehicleRequest();
        deleteVehicleRequest.setUserId(Prefs.getString(PrefsKeys.USERID, ""));
        deleteVehicleRequest.setUserType(Prefs.getString(PrefsKeys.USER_TYPE, ""));
        deleteVehicleRequest.setVehicleId("3");
        WebAPIClient.getInstance(context).delete_vehicle(deleteVehicleRequest, new Callback<DeleteVehicleResponse>() {
            @Override
            public void onResponse(Call<DeleteVehicleResponse> call, Response<DeleteVehicleResponse> response) {
                // rlLoading.setVisibility(View.GONE);

                DeleteVehicleResponse deleteVehicleResponse = response.body();
                if (deleteVehicleResponse.getFlag().equals("true")) {
                    Log.d("delete", "vehicle successfully deleted!");
                }
            }

            @Override
            public void onFailure(Call<DeleteVehicleResponse> call, Throwable t) {
                Toast.makeText(context, "delete_vehicle" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //rlLoading.setVisibility(View.GONE);
            }
        });
    }

    private void gotoFragDetails(GetVehicleData getVehicleData) {
        FragmentTransaction ft = ((ActHomeUser) context).getSupportFragmentManager().beginTransaction();
        FragRegisterVehicle fragRegisterVehicle = new FragRegisterVehicle();
        fragRegisterVehicle.setGetVehicleData(getVehicleData);
        ft.addToBackStack(FragRegisterVehicle.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragRegisterVehicle, FragRegisterVehicle.class.getName());
        ft.commit();
    }
}


