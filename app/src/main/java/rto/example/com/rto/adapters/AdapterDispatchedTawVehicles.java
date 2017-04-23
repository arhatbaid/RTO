package rto.example.com.rto.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import rto.example.com.rto.R;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.DispatchedTawVehicle;

public class AdapterDispatchedTawVehicles extends ArrayAdapter<DispatchedTawVehicle> {
    private Context context;
    private ArrayList<DispatchedTawVehicle> arrayDispatchedTawVehicles;
    private int[] images = null;


    public AdapterDispatchedTawVehicles(Context context, int resource, ArrayList<DispatchedTawVehicle> arrayDispatchedTawVehicles) {
        super(context, 0, arrayDispatchedTawVehicles);
        this.context = context;
        this.arrayDispatchedTawVehicles = arrayDispatchedTawVehicles;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_vehicle_dispatch, parent, false);
            v.lblTawTime = (TextView) convertView.findViewById(R.id.lblTawTime);
            v.lblDispatchedTime = (TextView) convertView.findViewById(R.id.lblDispatchedTime);
            v.lblVehicleNumberPlate = (TextView) convertView.findViewById(R.id.lblVehicleNumberPlate);
            v.btnDispatch = (Button) convertView.findViewById(R.id.btnDispatch);
            v.lblVehicleType = (TextView) convertView.findViewById(R.id.lblVehicleType);
            v.lblChallanAmmount = (TextView) convertView.findViewById(R.id.lblChallanAmmount);
            v.lblChallanNumber = (TextView) convertView.findViewById(R.id.lblChallanNumber);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        try {
            v.lblDispatchedTime.setText(arrayDispatchedTawVehicles.get(position).getDispatchedTime()+"");
            v.lblTawTime.setText(arrayDispatchedTawVehicles.get(position).getAddedOn());
            v.lblVehicleNumberPlate.setText(arrayDispatchedTawVehicles.get(position).getVehicleNumberPlate());
            v.lblVehicleType.setText(arrayDispatchedTawVehicles.get(position).getVehicleType().equals("1")?"Bike":"Car");
            v.lblChallanAmmount.setText(arrayDispatchedTawVehicles.get(position).getAmount());
            v.lblChallanNumber.setText(arrayDispatchedTawVehicles.get(position).getChallanNumber().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    private class ViewHolder {
        TextView lblVehicleNumberPlate;
        TextView lblVehicleType;
        TextView lblChallanAmmount;
        TextView lblChallanNumber;
        TextView lblDispatchedTime;
        TextView lblTawTime;
        Button btnDispatch;
    }


}
