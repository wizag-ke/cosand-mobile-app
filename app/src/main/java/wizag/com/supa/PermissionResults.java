package wizag.com.supa;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 07/05/2018.
 */

public class PermissionResults {
    @SerializedName("permission")
    private String  permission;

    public PermissionResults() {
    }

    public PermissionResults(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}



