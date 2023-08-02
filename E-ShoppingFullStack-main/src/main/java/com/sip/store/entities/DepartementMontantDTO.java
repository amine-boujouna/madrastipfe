package com.sip.store.entities;

public class DepartementMontantDTO {
    private String departement;
    private String username;
    private Double totalMontant;

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTotalMontant() {
        return totalMontant;
    }

    public void setTotalMontant(Double totalMontant) {
        this.totalMontant = totalMontant;
    }
}
