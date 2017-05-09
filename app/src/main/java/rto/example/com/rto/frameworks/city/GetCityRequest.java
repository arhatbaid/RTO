
package rto.example.com.rto.frameworks.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCityRequest {


    @SerializedName("state_id")
    @Expose
    private String stateId;


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

}
