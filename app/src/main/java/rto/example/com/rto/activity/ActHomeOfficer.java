package rto.example.com.rto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.pixplicity.easyprefs.library.Prefs;

import net.simonvt.menudrawer.MenuDrawer;

import rto.example.com.rto.R;
import rto.example.com.rto.adapters.AdapterMenu;
import rto.example.com.rto.fragment.FragHomeUser;
import rto.example.com.rto.fragment.FragRegisterVehicle;
import rto.example.com.rto.fragment.FragSearchTawVehicle;
import rto.example.com.rto.fragment.FragVehicleList;

public class ActHomeOfficer extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener {

    private MenuDrawer mDrawer;
    private ListView lstItem;
    private ImageView imgMenu = null;

    private AdapterMenu adapter;
    private String[] menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_user);
        mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
        mDrawer.setContentView(R.layout.act_home_user);
        mDrawer.setMenuView(R.layout.menu_bar);

        getSupportFragmentManager().beginTransaction().add(R.id.fragContainer, new FragHomeUser(), FragHomeUser.class.getName()).commit();

        lstItem = (ListView) findViewById(R.id.lstItem);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        menuItem = getResources().getStringArray(R.array.menu_item);

        adapter = new AdapterMenu(getApplicationContext(), 0, menuItem);
        lstItem.setAdapter(adapter);

        lstItem.setOnItemClickListener(this);
        imgMenu.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                mDrawer.closeMenu();
                FragVehicleList fragVehicleList = (FragVehicleList) getSupportFragmentManager().findFragmentByTag(FragVehicleList.class.getName());
                if (fragVehicleList != null && fragVehicleList.isVisible())
                    return;
                FragVehicleList fragVehicleList1 = new FragVehicleList();
                ft.addToBackStack(FragVehicleList.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainer, fragVehicleList1, FragVehicleList.class.getName());
                ft.commit();
                break;
            case 1:
                mDrawer.closeMenu();
                FragSearchTawVehicle fragSearchTawVehicle = (FragSearchTawVehicle) getSupportFragmentManager().findFragmentByTag(FragSearchTawVehicle.class.getName());
                if (fragSearchTawVehicle != null && fragSearchTawVehicle.isVisible())
                    return;
                FragSearchTawVehicle fragSearchTawVehicle1 = new FragSearchTawVehicle();
                ft.addToBackStack(FragSearchTawVehicle.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainer, fragSearchTawVehicle1, FragSearchTawVehicle.class.getName());
                ft.commit();
                break;
            case 2:
                mDrawer.closeMenu();
                FragHomeUser fragHomeUser = (FragHomeUser) getSupportFragmentManager().findFragmentByTag(FragHomeUser.class.getName());
                if (fragHomeUser != null && fragHomeUser.isVisible())
                    return;
                FragHomeUser fragHomeUser1 = new FragHomeUser();
                ft.addToBackStack(FragHomeUser.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainer, fragHomeUser1, FragHomeUser.class.getName());
                ft.commit();
                break;
            case 3:
                //mDrawer.closeMenu();
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        Prefs.clear();
                        finish();
                        startActivity(new Intent(ActHomeOfficer.this, ActLoginSignUp.class));
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

    @Override
    public void onClick(View view) {
        mDrawer.openMenu();
    }
}
