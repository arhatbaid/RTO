package rto.example.com.rto.eventbus.messages;

/**
 * Created by iRoid8 on 10/4/2016.
 */
public class GetAnswer extends BusMessage {

    private int position = 0;
    private String option="";
    private String type="";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
