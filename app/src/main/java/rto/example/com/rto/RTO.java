package rto.example.com.rto;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by ridz1 on 13/04/2017.
 */

public class RTO extends Application {

    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
