package rto.example.com.rto.eventbus;


import rto.example.com.rto.RTO;
import rto.example.com.rto.eventbus.messages.BusMessage;

/**
 * Created by James P. Zimmerman II on 3/14/16.
 */
public class AppEventBus {
    public static final String TAG = AppEventBus.class.getSimpleName();

    private static org.greenrobot.eventbus.EventBus bus() {
        return RTO.getEventBus();
    }

    public static void post(BusMessage message) {
        AppEventBus.bus().post(message);
    }

    public static void register(Object target) {
        AppEventBus.bus().register(target);
    }

    public static void unregister(Object target) {
        AppEventBus.bus().unregister(target);
    }
}
