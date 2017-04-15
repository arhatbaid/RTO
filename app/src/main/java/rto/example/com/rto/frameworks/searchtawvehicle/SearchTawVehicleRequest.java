
package rto.example.com.rto.frameworks.searchtawvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchTawVehicleRequest {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("vehicle_state_id")
    @Expose
    private String vehicleStateId;
    @SerializedName("vehicle_city_id")
    @Expose
    private String vehicleCityId;
    @SerializedName("vehicle_series_no")
    @Expose
    private String vehicleSeriesNo;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;

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

    public String getVehicleStateId() {
        return vehicleStateId;
    }

    public void setVehicleStateId(String vehicleStateId) {
        this.vehicleStateId = vehicleStateId;
    }

    public String getVehicleCityId() {
        return vehicleCityId;
    }

    public void setVehicleCityId(String vehicleCityId) {
        this.vehicleCityId = vehicleCityId;
    }

    public String getVehicleSeriesNo() {
        return vehicleSeriesNo;
    }

    public void setVehicleSeriesNo(String vehicleSeriesNo) {
        this.vehicleSeriesNo = vehicleSeriesNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

}
