package com.tcc.luis.poster.api;

import com.tcc.luis.poster.api.model.Cidade;
import com.tcc.luis.poster.api.model.Estado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UrlService {

    @GET ("https://servicodados.ibge.gov.br/api/v1/localidades/estados/")
    Call<List<Estado>> pegarEstados();

    @GET ("https://servicodados.ibge.gov.br/api/v1/localidades/estados/{UF}/municipios")
    Call<List<Cidade>> pegarCidadesPoEstados(@Path("UF") int idEstado);
}
