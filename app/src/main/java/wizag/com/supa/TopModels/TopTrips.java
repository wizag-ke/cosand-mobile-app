package wizag.com.supa.TopModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wizag.com.supa.ModelResults.TripsResults;
import wizag.com.supa.PermissionResults;

/**
 * Created by User on 08/05/2018.
 */

public class TopTrips {
    @SerializedName("Permissions")
    private List<TripsResults> listPermissionsM;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public TopTrips(List<TripsResults> listPermissionsM, int status, String message) {
        this.listPermissionsM = listPermissionsM;
        this.status = status;
        this.message = message;
    }

    public TopTrips() {
    }

    public List<TripsResults> getListPermissionsM() {
        return listPermissionsM;
    }

    public void setListPermissionsM(List<TripsResults> listPermissionsM) {
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
