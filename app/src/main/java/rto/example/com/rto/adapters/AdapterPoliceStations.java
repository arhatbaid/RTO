

package rto.example.com.rto.adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rto.example.com.rto.R;
import rto.example.com.rto.activity.ActHomeUser;
import rto.example.com.rto.fragment.FragMap;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationData;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleData;
import rto.example.com.rto.helper.AppHelper;

/**
 * Created by ridz1 on 13/04/2017.
 */

public class AdapterPoliceStations extends RecyclerView.Adapter<AdapterPoliceStations.ViewHolder> {

    private ArrayList<GetNearPoliceStationData> objects = new ArrayList<>();
    private Context context;

    public AdapterPoliceStations(Context context) {
        this.context = context;
    }

    public void addAll(ArrayList<GetNearPoliceStationData> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_police_station, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        if (AppHelper.isValidString(objects.get(position).getName()))
            holder.lblName.setText(objects.get(position).getName());

        if (AppHelper.isValidString(objects.get(position).getAddress()))
            holder.lblAddress.setText(objects.get(position).getAddress());

        if (AppHelper.isValidString(objects.get(position).getDistance()))
            holder.lblDistance.setText(objects.get(position).getDistance()+" KM");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFragMap(objects.get(position));
            }
        });

    }

    private void gotoFragMap(GetNearPoliceStationData getNearPoliceStationData) {
        FragmentTransaction ft = ((ActHomeUser) context).getSupportFragmentManager().beginTransaction();
        FragMap fragMap = new FragMap();
        fragMap.setGetNearPoliceStationData(getNearPoliceStationData);
        // fragMap.setArrPoliceStations(getNearPoliceStationDatas);
        ft.addToBackStack(FragMap.class.getName());
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right);
        ft.replace(R.id.fragContainer, fragMap, FragMap.class.getName());
        ft.commit();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lblName;
        TextView lblAddress;
        TextView lblDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            lblName = (TextView) itemView.findViewById(R.id.lblName);
            lblAddress = (TextView) itemView.findViewById(R.id.lblAddress);
            lblDistance = (TextView) itemView.findViewById(R.id.lblDistance);

        }
    }

}


