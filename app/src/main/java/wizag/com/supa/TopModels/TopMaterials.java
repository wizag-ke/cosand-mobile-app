package wizag.com.supa.TopModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wizag.com.supa.ModelResults.MaterialResults;

/**
 * Created by User on 18/05/2018.
 */

public class TopMaterials {
    @SerializedName("Name")
    private List<MaterialResults> listPermissionsM;
    @SerializedName("success")
    private int success;


    public TopMaterials(List<MaterialResults> listPermissionsM, int success) {
        this.listPermissionsM = listPermissionsM;
        this.success = success;

    }

    public TopMaterials() {
    }

    public List<MaterialResults> getListPermissionsM() {
        return listPermissionsM;
    }

    public void setListPermissionsM(List<MaterialResults> listPermissionsM) {
        this.listPermissionsM = listPermissionsM;
    }

    public int getStatus() {
        return success;
    }

    public void setStatus(int success) {
        this.success = success;
    }

}
