package wizag.com.supa;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by User on 12/04/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("oauth/token")
    Call<AuthUser> loginUser(@Field("username") String username,
                             @Field("password") String password,
                             @Field("grant_type") String grant_type,
                             @Field("client_id") String client_id,
                             @Field("client_secret") String client_secret);

    @FormUrlEncoded
    @POST("api/register")
    Call<AuthUser> registerUser(@Field("email") String email,
                                @Field("id_no") String id_no,
                                @Field("password") String password,
                                @Field("fname") String fname,
                                @Field("lname") String lname,
                                @Field("phone") String phone,
                                @Field("role_id") int role_id);
}
