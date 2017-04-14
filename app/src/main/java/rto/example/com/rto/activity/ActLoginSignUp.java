package rto.example.com.rto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import rto.example.com.rto.R;
import rto.example.com.rto.fragment.FragLogin;
import rto.example.com.rto.helper.PrefsKeys;

public class ActLoginSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Prefs.getString(PrefsKeys.USERID, "").isEmpty()) {
            if (Prefs.getString(PrefsKeys.USER_TYPE, "").equals("3")) {
                startActivity(new Intent(this, ActHomeUser.class));
            } else {
                startActivity(new Intent(this, ActHomeOfficer.class));
            }
            finish();
        }
        setContentView(R.layout.act_login_sign_up);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, new FragLogin(), FragLogin.class.getName()).commit();
    }
}
