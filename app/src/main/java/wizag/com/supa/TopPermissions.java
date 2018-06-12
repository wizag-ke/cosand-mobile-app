package wizag.com.supa;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 07/05/2018.
 */

public class TopPermissions {
    @SerializedName("Permissions")
    private List<PermissionResults> listPermissionsM;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public TopPermissions(List<PermissionResults> listPermissionsM, int status, String message) {
        this.listPermissionsM = listPermissionsM;
        this.status = status;
        this.message = message;
    }

    public TopPermissions() {
    }

    public List<PermissionResults> getListPermissionsM() {
        return listPermissionsM;
    }

    public void setListPermissionsM(List<PermissionResults> listPermissionsM) {
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