package rto.example.com.rto.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_vehicle_dispatch, parent, false);
            v.lblTime = (TextView) convertView.findViewById(R.id.lblTime);
            v.lblVehicleNumberPlate = (TextView) convertView.findViewById(R.id.lblVehicleNumberPlate);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v.lblTime.setText(arrayDispatchedTawVehicles.get(position).getAddedOn());
        v.lblVehicleNumberPlate.setText(arrayDispatchedTawVehicles.get(position).getVehicleNumberPlate());

        return convertView;
    }

    private class ViewHolder {
        TextView lblVehicleNumberPlate;
        TextView lblTime;
    }
}
