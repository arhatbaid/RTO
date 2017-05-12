
package rto.example.com.rto.frameworks.searchtawvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchTawVehicleData {

    @SerializedName("taw_id")
    @Expose
    private String tawId;
    @SerializedName("officer_id")
    @Expose
    private String officerId;
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
    @SerializedName("taw_address")
    @Expose
    private String tawAddress;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("dispatched_time")
    @Expose
    private Object dispatchedTime;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("challan_number")
    @Expose
    private Object challanNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("officer_name")
    @Expose
    private String officerName;

    public String getTawId() {
        return tawId;
    }

    public void setTawId(String tawId) {
        this.tawId = tawId;
    }

    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
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

    public String getTawAddress() {
        return tawAddress;
    }

    public void setTawAddress(String tawAddress) {
        this.tawAddress = tawAddress;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Object getDispatchedTime() {
        return dispatchedTime;
    }

    public void setDispatchedTime(Object dispatchedTime) {
        this.dispatchedTime = dispatchedTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Object getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(Object challanNumber) {
        this.challanNumber = challanNumber;
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

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

}
