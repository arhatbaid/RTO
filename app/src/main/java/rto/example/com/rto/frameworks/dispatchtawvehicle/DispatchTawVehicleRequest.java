
package rto.example.com.rto.frameworks.dispatchtawvehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DispatchTawVehicleRequest {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("taw_ids")
    @Expose
    private String tawIds;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("challan_number")
    @Expose
    private String challanNumber;

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

    public String getTawIds() {
        return tawIds;
    }

    public void setTawIds(String tawIds) {
        this.tawIds = tawIds;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

}
