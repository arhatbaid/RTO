package rto.example.com.rto.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pixplicity.easyprefs.library.Prefs;

import rto.example.com.rto.R;
import rto.example.com.rto.helper.PrefsKeys;

public class ActSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContet();
    }

    private void setContet() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!Prefs.getString(PrefsKeys.USERID, "").equals("")) {
                    startActivity(new Intent(ActSplash.this, ActHomeUser.class));
                } else
                    startActivity(new Intent(ActSplash.this, ActLoginSignUp.class));
                finish();
            }
        }, 2 * 1000);
    }
}
