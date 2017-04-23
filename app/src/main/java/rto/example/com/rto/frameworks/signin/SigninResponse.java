
package rto.example.com.rto.frameworks.signin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SigninResponse {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("result")
    @Expose
    private String result;@SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private SigninData data;

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

    public SigninData getData() {
        return data;
    }

    public void setData(SigninData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
