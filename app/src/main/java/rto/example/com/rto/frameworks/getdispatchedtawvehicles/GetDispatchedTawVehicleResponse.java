
package rto.example.com.rto.frameworks.getdispatchedtawvehicles;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDispatchedTawVehicleResponse {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<DispatchedTawVehicle> data = null;

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

    public List<DispatchedTawVehicle> getData() {
        return data;
    }

    public void setData(List<DispatchedTawVehicle> data) {
        this.data = data;
    }

}
