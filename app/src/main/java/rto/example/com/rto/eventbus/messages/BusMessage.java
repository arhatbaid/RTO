package rto.example.com.rto.eventbus.messages;

import java.util.Date;

/**
 * Created by James P. Zimmerman II on 3/14/16.
 */
public class BusMessage {
    public Date created;
    public BusMessage() {
        this.created = new Date();
    }
}
