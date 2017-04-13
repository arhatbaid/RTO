
package rto.example.com.rto.frameworks.editvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditVehicleRequest {

    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("vehicle_series_no")
    @Expose
    private String vehicleSeriesNo;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("buying_date")
    @Expose
    private String buyingDate;
    @SerializedName("puc_number")
    @Expose
    private String pucNumber;
    @SerializedName("puc_purchase_date")
    @Expose
    private String pucPurchaseDate;

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

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(String buyingDate) {
        this.buyingDate = buyingDate;
    }

    public String getPucNumber() {
        return pucNumber;
    }

    public void setPucNumber(String pucNumber) {
        this.pucNumber = pucNumber;
    }

    public String getPucPurchaseDate() {
        return pucPurchaseDate;
    }

    public void setPucPurchaseDate(String pucPurchaseDate) {
        this.pucPurchaseDate = pucPurchaseDate;
    }

}
