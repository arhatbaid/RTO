
package rto.example.com.rto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rto.example.com.rto.R;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleData;
import rto.example.com.rto.helper.AppHelper;

/**
 * Created by ridz1 on 13/04/2017.
 */

public class AdapterTawVehicles extends RecyclerView.Adapter<AdapterTawVehicles.ViewHolder> {

    private ArrayList<GetOfficerTawVehicleData> arrVehicle;
    private Context context;
    private OnDispatchPressed onDispatchPressed;

    public AdapterTawVehicles(ArrayList<GetOfficerTawVehicleData> arrVehicle, Context context, OnDispatchPressed onDispatchPressed) {
        this.arrVehicle = arrVehicle;
        this.context = context;
        this.onDispatchPressed = onDispatchPressed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_taw_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if (AppHelper.isValidString(arrVehicle.get(position).getVehicleNumberPlate()))
            holder.lblVehicleName.setText(arrVehicle.get(position).getVehicleNumberPlate());

        if (AppHelper.isValidString(arrVehicle.get(position).getVehicleNumberPlate()))
            holder.lblVehicleNumberPlate.setText(arrVehicle.get(position).getModifiedOn());


        try {
          // holder.lblDispatchedTime.setText(arrVehicle.get(position).getDispatchedTime()+"");
           holder.lblTawTime.setText(arrVehicle.get(position).getAddedOn()+"");
           holder.lblVehicleType.setText(arrVehicle.get(position).getVehicleType().equals("1")?"Bike":"Car");
           holder.lblChallanAmmount.setText(arrVehicle.get(position).getAmount()+"");
           holder.lblChallanNumber.setText(arrVehicle.get(position).getChallanNumber().toString()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }


       

        holder.btnDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDispatchPressed.onDispatchClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrVehicle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblVehicleName;
        Button btnDispatch;
        ImageView imgDelete;

        TextView lblVehicleNumberPlate;
        TextView lblVehicleType;
        TextView lblChallanAmmount;
        TextView lblChallanNumber;
        TextView lblDispatchedTime;
        TextView lblTawTime;


        public ViewHolder(View itemView) {
            super(itemView);
            lblVehicleName = (TextView) itemView.findViewById(R.id.lblVehicleName);
            lblVehicleNumberPlate = (TextView) itemView.findViewById(R.id.lblVehicleNumberPlate);
            btnDispatch = (Button) itemView.findViewById(R.id.btnDispatch);
            lblTawTime = (TextView) itemView.findViewById(R.id.lblTawTime);
            lblDispatchedTime = (TextView) itemView.findViewById(R.id.lblDispatchedTime);
            lblVehicleNumberPlate = (TextView) itemView.findViewById(R.id.lblVehicleNumberPlate);
            btnDispatch = (Button) itemView.findViewById(R.id.btnDispatch);
            lblVehicleType = (TextView) itemView.findViewById(R.id.lblVehicleType);
            lblChallanAmmount = (TextView) itemView.findViewById(R.id.lblChallanAmmount);
            lblChallanNumber = (TextView) itemView.findViewById(R.id.lblChallanNumber);


        }
    }





    public interface  OnDispatchPressed{
        public void onDispatchClick(int pos);
    }
}


