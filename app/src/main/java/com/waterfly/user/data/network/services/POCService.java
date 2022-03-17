package com.waterfly.user.data.network.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class POCService {

    private static final String BASE_URL = "http://econnecto.com/";
    private ServiceApi mServiceApi;
    private static POCService instance;

    public static POCService getInstance() {
        if (instance == null) {
            instance = new POCService();
        }
        return instance;
    }

    private POCService() {
//        OkHttpClient httpClient = new OkHttpClient();
//        httpClient.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request.Builder requestBuilder = chain.request().newBuilder();
//                requestBuilder.header("Content-Type", "application/json");
//                return chain.proceed(requestBuilder.build());
//            }
//        });
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mServiceApi = mRetrofit.create(ServiceApi.class);
    }

    public ServiceApi getApiService() {
        return mServiceApi;
    }

}
