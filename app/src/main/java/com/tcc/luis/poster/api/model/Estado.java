package com.tcc.luis.poster.api.model;

import androidx.annotation.NonNull;

public class Estado implements Comparable<Estado> {

    private int id;
    private String sigla;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int compareTo(Estado o) {
        return sigla.compareTo(o.getSigla());
    }

    @NonNull
    @Override
    public String toString() {
        return getSigla();
    }
}
