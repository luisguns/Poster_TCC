package com.tcc.luis.poster.model;

import java.util.HashMap;

public class Competencia {

    private String competencia;

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public HashMap toHash(){
        HashMap<String, String> hm = new HashMap<>();
        hm.put("competencia", this.getCompetencia());
        return hm;
    }
}
