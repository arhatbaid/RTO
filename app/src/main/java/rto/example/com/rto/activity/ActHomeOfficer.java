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
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import net.simonvt.menudrawer.MenuDrawer;

import rto.example.com.rto.R;
import rto.example.com.rto.adapters.AdapterMenu;
import rto.example.com.rto.fragment.FragAddTawVehicle;
import rto.example.com.rto.fragment.FragGetDispatchedTawVehicle;
import rto.example.com.rto.fragment.FragSearchTawVehicle;
import rto.example.com.rto.fragment.FragTawVehicleList;
import rto.example.com.rto.fragment.FragVehicleList;

public class ActHomeOfficer extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener {

    private MenuDrawer mDrawer;
    private ListView lstItem;
    private ImageView imgMenu = null;
    private TextView lblTitle ;

    private AdapterMenu adapter;
    private String[] menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home_user);
        mDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY);
        mDrawer.setContentView(R.layout.act_home_officer);
        mDrawer.setMenuView(R.layout.menu_bar);

        getSupportFragmentManager().beginTransaction().add(R.id.fragContainerOfficer, new FragAddTawVehicle(), FragAddTawVehicle.class.getName()).commit();

        lstItem = (ListView) findViewById(R.id.lstItem);
        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        menuItem = getResources().getStringArray(R.array.menu_item_officer);

        adapter = new AdapterMenu(getApplicationContext(), 0, menuItem);
        lstItem.setAdapter(adapter);

        lstItem.setOnItemClickListener(this);
        imgMenu.setOnClickListener(this);
    }

    public void setActTitle(String text) {
        lblTitle.setText(text);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 2:
                mDrawer.closeMenu();
                FragTawVehicleList fragTawVehicleList = (FragTawVehicleList) getSupportFragmentManager().findFragmentByTag(FragTawVehicleList.class.getName());
                if (fragTawVehicleList != null && fragTawVehicleList.isVisible())
                    return;
                FragTawVehicleList fragTawVehicleList1 = new FragTawVehicleList();
                ft.addToBackStack(FragTawVehicleList.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainerOfficer, fragTawVehicleList1, FragTawVehicleList.class.getName());
                ft.commit();
                break;
            case 1:
                mDrawer.closeMenu();
                FragGetDispatchedTawVehicle fragEditRegisterVehicle = (FragGetDispatchedTawVehicle) getSupportFragmentManager().findFragmentByTag(FragGetDispatchedTawVehicle.class.getName());
                if (fragEditRegisterVehicle != null && fragEditRegisterVehicle.isVisible())
                    return;
                FragGetDispatchedTawVehicle fragEditRegisterVehicle1 = new FragGetDispatchedTawVehicle();
                ft.addToBackStack(FragGetDispatchedTawVehicle.class.getName());
                FragSearchTawVehicle fragSearchTawVehicle1 = new FragSearchTawVehicle();
                ft.addToBackStack(FragSearchTawVehicle.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainerOfficer, fragSearchTawVehicle1, FragSearchTawVehicle.class.getName());
                ft.replace(R.id.fragContainerOfficer, fragEditRegisterVehicle1, FragGetDispatchedTawVehicle.class.getName());
                ft.commit();
                break;
            case 0:
                mDrawer.closeMenu();
                FragAddTawVehicle fragAddTawVehicle = (FragAddTawVehicle) getSupportFragmentManager().findFragmentByTag(FragAddTawVehicle.class.getName());
                if (fragAddTawVehicle != null && fragAddTawVehicle.isVisible())
                    return;
                FragAddTawVehicle fragAddTawVehicle1 = new FragAddTawVehicle();
                ft.addToBackStack(FragAddTawVehicle.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainerOfficer, fragAddTawVehicle1, FragAddTawVehicle.class.getName());
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
