package com.tcc.luis.poster.model;

public class Usuario{
    private String name;
    private String lastName;
    private String email;
    private boolean isGoogleUser;
    private String ocupacao;
    private boolean isCompanyRepresentative;
    private String uidUser;
    private String profileImage;
    private String sobre;
    private boolean companyCompleteRegister;

    public boolean isCompanyCompleteRegister() {
        return companyCompleteRegister;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getSobre() {
        return sobre;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public void setCompanyCompleteRegister(boolean companyCompleteRegister) {
        this.companyCompleteRegister = companyCompleteRegister;
    }

    public boolean isCompanyRepresentative() {
        return isCompanyRepresentative;
    }

    public void setCompanyRepresentative(boolean companyRepresentative) {
        isCompanyRepresentative = companyRepresentative;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }
}
