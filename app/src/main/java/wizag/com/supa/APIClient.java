package wizag.com.supa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 12/04/2018.
 */

public class APIClient {
    public static final String BASE_URL = "http://sduka.wizag.biz/";
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {

            Gson gson = new GsonBuilder().setLenient().create();
            if (retrofit==null) {
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
}