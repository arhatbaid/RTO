
package rto.example.com.rto.frameworks.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupRespose {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private SignupData data;
    @SerializedName("msg")
    @Expose
    private String msg;

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

    public SignupData getData() {
        return data;
    }

    public void setData(SignupData data) {
        this.data = data;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
