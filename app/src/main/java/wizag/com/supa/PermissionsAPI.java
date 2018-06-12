package wizag.com.supa;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 07/05/2018.
 */

public class PermissionsAPI {
    private static Retrofit retrofit = null;

    private static OkHttpClient buildClient(){
        return new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public static Retrofit getPermissions(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder().client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://jijikenya.co.ke/")
                    .build();
        }

        return retrofit;
    }
}
