package com.tcc.luis.poster.model;

public class OportunidadeDeEmprego {
    private String key;
    private Empresa empresa;
    private String cargo;
    private String descricao;
    private String categoria;
    private String empresaUid;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEmpresaUid() {
        return empresaUid;
    }

    public void setEmpresaUid(String empresaUid) {
        this.empresaUid = empresaUid;
    }
}
