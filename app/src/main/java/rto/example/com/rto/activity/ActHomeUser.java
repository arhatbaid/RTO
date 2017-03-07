package rto.example.com.rto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import net.simonvt.menudrawer.MenuDrawer;

import rto.example.com.rto.R;
import rto.example.com.rto.adapters.AdapterMenu;
import rto.example.com.rto.fragment.FragHomeUser;
import rto.example.com.rto.fragment.FragRegisterVehicle;
import rto.example.com.rto.fragment.FragVehicleList;

public class ActHomeUser extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
View.OnClickListener{

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
                FragRegisterVehicle fragRegisterVehicle = (FragRegisterVehicle) getSupportFragmentManager().findFragmentByTag(FragRegisterVehicle.class.getName());
                if (fragRegisterVehicle != null && fragRegisterVehicle.isVisible())
                    return;
                FragRegisterVehicle fragRegisterVehicle1 = new FragRegisterVehicle();
                ft.addToBackStack(FragRegisterVehicle.class.getName());
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_in_right, R.anim.slide_out_right);
                ft.replace(R.id.fragContainer, fragRegisterVehicle1, FragRegisterVehicle.class.getName());
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
        }
    }

    @Override
    public void onClick(View view) {
        mDrawer.openMenu();
    }
}
