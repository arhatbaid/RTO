
package rto.example.com.rto.frameworks.addvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVehicleResponse {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("result")
    @Expose
    private String result;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
