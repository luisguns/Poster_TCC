package com.tcc.luis.poster.api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerGenerateLocalidades {
    private final Context context;
    private String BASE_URL_SERVIDOR = "https://servicodados.ibge.gov.br/api/v1/localidades/";

    public ServerGenerateLocalidades(Context context){
        this.context = context;
    }

    public UrlService criarServer(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_SERVIDOR)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UrlService service = retrofit.create(UrlService.class);
        return  service;
    }
}
