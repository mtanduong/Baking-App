package com.example.bakingapp.utils;


public class BakingApi {

    //https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    static final String BAKING_PATH = "topher/2017/May/59121517_baking/baking.json";

    public static BakingService createService() {
        return RetrofitClient.getClient(BASE_URL).create(BakingService.class);
    }
}
