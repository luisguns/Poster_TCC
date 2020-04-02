package com.tcc.luis.poster.model;

public class Empresa {

    private String imagemLogo;
    private String representanteId;
    private String setor;
    private String nome;
    private String cnpj;
    private String slogan;
    private Endereco endereco;
    private String telefone;

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Empresa(){
        this.imagemLogo = "";
    }

    public String getImagemLogo() {
        return imagemLogo;
    }

    public void setImagemLogo(String imagemLogo) {
        this.imagemLogo = imagemLogo;
    }

    public String getNome() {
        return nome;
    }

    public String getRepresentanteId() {
        return representanteId;
    }


    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public void setRepresentanteId(String representanteId) {
        this.representanteId = representanteId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
