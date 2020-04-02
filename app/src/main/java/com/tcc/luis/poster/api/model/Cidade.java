package com.tcc.luis.poster.api.model;

import androidx.annotation.NonNull;

public class Cidade implements Comparable<Cidade> {

    private int id;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NonNull
    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int compareTo(Cidade o) {
        return nome.compareTo(o.getNome());
    }
}
