package com.tcc.luis.poster.model;

public class Endereco {
    private String estadoSigla;
    private String cidadeNome;
    private String Bairro;
    private String Rua;

    public Endereco(String estadoSigla, String cidadeNome, String bairro, String rua) {
        this.estadoSigla = estadoSigla;
        this.cidadeNome = cidadeNome;
        Bairro = bairro;
        Rua = rua;
    }

    public Endereco(){

    }

    public String getEstadoSigla() {
        return estadoSigla;
    }

    public void setEstadoSigla(String estadoSigla) {
        this.estadoSigla = estadoSigla;
    }

    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String rua) {
        Rua = rua;
    }
}
