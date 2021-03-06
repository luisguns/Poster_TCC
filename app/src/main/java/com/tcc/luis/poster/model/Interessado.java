package com.tcc.luis.poster.model;

public class Interessado {
    private String idInteressado;
    private String idEmpresa;
    private String idOportunidade;
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getIdOportunidade() {
        return idOportunidade;
    }

    public void setIdOportunidade(String idOportunidade) {
        this.idOportunidade = idOportunidade;
    }

    public String getIdInteressado() {
        return idInteressado;
    }

    public void setIdInteressado(String idInteressado) {
        this.idInteressado = idInteressado;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
