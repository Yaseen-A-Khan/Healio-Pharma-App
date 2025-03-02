package com.example.madfour;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("predict")
    Call<DiseaseResponse> predictDisease(@Body SymptomRequest symptomRequest);


}
