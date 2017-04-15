
package rto.example.com.rto.frameworks.addtawvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTawVehicleRequest {

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
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("taw_address")
    @Expose
    private String tawAddress;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("check_second_time")
    @Expose
    private String checkSecondTime;

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

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTawAddress() {
        return tawAddress;
    }

    public void setTawAddress(String tawAddress) {
        this.tawAddress = tawAddress;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCheckSecondTime() {
        return checkSecondTime;
    }

    public void setCheckSecondTime(String checkSecondTime) {
        this.checkSecondTime = checkSecondTime;
    }

}
