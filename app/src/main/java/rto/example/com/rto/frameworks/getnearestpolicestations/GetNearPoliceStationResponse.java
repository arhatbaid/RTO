
package rto.example.com.rto.frameworks.getnearestpolicestations;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNearPoliceStationResponse {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<GetNearPoliceStationData> data = null;

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

    public List<GetNearPoliceStationData> getData() {
        return data;
    }

    public void setData(List<GetNearPoliceStationData> data) {
        this.data = data;
    }

}
