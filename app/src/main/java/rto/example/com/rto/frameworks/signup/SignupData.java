
package rto.example.com.rto.frameworks.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupData {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
