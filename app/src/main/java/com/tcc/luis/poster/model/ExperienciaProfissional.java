package com.tcc.luis.poster.model;

import java.util.HashMap;

public class ExperienciaProfissional {
    private String cargo;
    private String empresa;
    private String descricao;
    private String dataInicio;
    private String dataFim;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public HashMap toHash(){
        HashMap<String, String> hs = new HashMap<>();
        hs.put("cargo",this.cargo);
        hs.put("empresa",this.empresa);
        hs.put("descricao",this.descricao);
        hs.put("dataInicio",this.dataInicio);
        hs.put("dataFim",this.dataFim);
        return hs;
    }
}
