
package rto.example.com.rto.frameworks.deletevehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteVehicleRequest {

    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
