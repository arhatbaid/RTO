package rto.example.com.rto.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rto.example.com.rto.R;
import rto.example.com.rto.fragment.FragLogin;

public class ActLoginSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_sign_up);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, new FragLogin(), FragLogin.class.getName()).commit();
    }
}
