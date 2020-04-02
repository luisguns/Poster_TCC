package com.tcc.luis.poster.model;

import java.util.ArrayList;
import java.util.List;

public class Curriculo {

    private String uidUsuario;
    private List<FormacaoAcademica> formacoesAcademicas;
    private List<Competencia> competencias;
    private List<ExperienciaProfissional> experienciasProfissionais;

    public Curriculo(String uidUsuario, List<FormacaoAcademica> formacoesAcademicas, List<Competencia> competencias) {
        this.uidUsuario = uidUsuario;
        this.formacoesAcademicas = formacoesAcademicas;
        this.competencias = competencias;
    }

    public List<ExperienciaProfissional> getExperienciasProfissionais() {
        return experienciasProfissionais;
    }

    public void setExperienciasProfissionais(List<ExperienciaProfissional> experienciasProfissionais) {
        this.experienciasProfissionais = experienciasProfissionais;
    }

    public Curriculo(){
        this.uidUsuario = uidUsuario;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public List<FormacaoAcademica> getFormacoesAcademicas() {
        return formacoesAcademicas;
    }

    public void setFormacoesAcademicas(List<FormacaoAcademica> formacoesAcademicas) {
        this.formacoesAcademicas = formacoesAcademicas;
    }

    public List<Competencia> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(List<Competencia> competencias) {
        this.competencias = competencias;
    }
}
