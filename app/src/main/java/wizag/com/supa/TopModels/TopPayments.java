package wizag.com.supa.TopModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wizag.com.supa.ModelResults.PaymentResults;
import wizag.com.supa.ModelResults.TripsResults;

/**
 * Created by User on 15/05/2018.
 */

public class TopPayments {
    @SerializedName("Payments")
    private List<PaymentResults> listPermissionsM;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public TopPayments(List<PaymentResults> listPermissionsM, int status, String message) {
        this.listPermissionsM = listPermissionsM;
        this.status = status;
        this.message = message;
    }

    public TopPayments() {
    }

    public List<PaymentResults> getListPermissionsM() {
        return listPermissionsM;
    }

    public void setListPermissionsM(List<PaymentResults> listPermissionsM) {
        this.listPermissionsM = listPermissionsM;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
