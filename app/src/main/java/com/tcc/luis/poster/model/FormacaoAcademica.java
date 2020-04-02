package com.tcc.luis.poster.model;

import java.util.HashMap;

public class FormacaoAcademica {

    private String usuario;
    private String nomeInstituicao;
    private String nomeCurso;
    private String tituloFormacao;
    private String dataConclusao;
    private String dataInicio;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String empresa) {
        this.usuario = empresa;
    }

    public String getTituloFormacao() {
        return tituloFormacao;
    }

    public void setTituloFormacao(String tituloFormacao) {
        this.tituloFormacao = tituloFormacao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(String dataConclusao) {
        this.dataConclusao = dataConclusao;
    }


    public HashMap toHash(){

        HashMap<String, String> hm = new HashMap<>();
        hm.put("usuario", this.getUsuario());
        hm.put("nomeCurso", this.getNomeCurso());
        hm.put("nomeInstituicao", this.getNomeInstituicao());
        hm.put("tituloFormacao", this.getTituloFormacao());
        hm.put("dataInicio", this.getDataInicio());
        hm.put("dataConclusao", this.getDataConclusao());

        return hm;
    };
}
