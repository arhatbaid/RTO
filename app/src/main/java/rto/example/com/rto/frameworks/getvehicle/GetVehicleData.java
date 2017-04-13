
package rto.example.com.rto.frameworks.getvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVehicleData {

    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicleNo;
    @SerializedName("vehicle_series_no")
    @Expose
    private String vehicleSeriesNo;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("buying_date")
    @Expose
    private String buyingDate;
    @SerializedName("puc_number")
    @Expose
    private String pucNumber;
    @SerializedName("puc_purchase_date")
    @Expose
    private String pucPurchaseDate;
    @SerializedName("is_puc_notify")
    @Expose
    private String isPucNotify;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("city_code")
    @Expose
    private String cityCode;
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @SerializedName("vehicle_number_plate")
    @Expose
    private String vehicleNumberPlate;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleSeriesNo() {
        return vehicleSeriesNo;
    }

    public void setVehicleSeriesNo(String vehicleSeriesNo) {
        this.vehicleSeriesNo = vehicleSeriesNo;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
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

    public String getIsPucNotify() {
        return isPucNotify;
    }

    public void setIsPucNotify(String isPucNotify) {
        this.isPucNotify = isPucNotify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getVehicleNumberPlate() {
        return vehicleNumberPlate;
    }

    public void setVehicleNumberPlate(String vehicleNumberPlate) {
        this.vehicleNumberPlate = vehicleNumberPlate;
    }

}
