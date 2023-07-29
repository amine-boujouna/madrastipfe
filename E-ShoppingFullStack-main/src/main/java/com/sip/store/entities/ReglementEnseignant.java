package com.sip.store.entities;

import javax.persistence.*;
import java.util.Date;
@Entity
public class ReglementEnseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titre;
    private String annee;
    private String moisdepais;
    private String datedepaimenet;
    private Long Matricule;
    private Long cin;
    private Long numcnss;
    private Long Categorie;
    private Long Echelon;
    private Long salairedebase;
    private Long Tauxhoraire;
    private Long prime;
    private Long heuresuplemaentaire;
    private Long congeeAnnuel;
    private Long retenueCNSS;
    private Long salairebrut;
    private Long deductions;
    private Long contributionsociale;
    private Long salairenet;
    private Long avance;
    private Long netapayer;
    private String modedepaimenet;
    private Long numcompte;
    private String intitulebanque;
    private Long congeprisparmois;
    private Long soldeconge;
    private Long nombredheuredabsence;
    @OneToOne
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getMoisdepais() {
        return moisdepais;
    }

    public void setMoisdepais(String moisdepais) {
        this.moisdepais = moisdepais;
    }

    public String getDatedepaimenet() {
        return datedepaimenet;
    }

    public void setDatedepaimenet(String datedepaimenet) {
        this.datedepaimenet = datedepaimenet;
    }

    public Long getMatricule() {
        return Matricule;
    }

    public void setMatricule(Long matricule) {
        Matricule = matricule;
    }

    public Long getCin() {
        return cin;
    }

    public void setCin(Long cin) {
        this.cin = cin;
    }

    public Long getNumcnss() {
        return numcnss;
    }

    public void setNumcnss(Long numcnss) {
        this.numcnss = numcnss;
    }

    public Long getCategorie() {
        return Categorie;
    }

    public void setCategorie(Long categorie) {
        Categorie = categorie;
    }

    public Long getEchelon() {
        return Echelon;
    }

    public void setEchelon(Long echelon) {
        Echelon = echelon;
    }

    public Long getSalairedebase() {
        return salairedebase;
    }

    public void setSalairedebase(Long salairedebase) {
        this.salairedebase = salairedebase;
    }

    public Long getTauxhoraire() {
        return Tauxhoraire;
    }

    public void setTauxhoraire(Long tauxhoraire) {
        Tauxhoraire = tauxhoraire;
    }

    public Long getPrime() {
        return prime;
    }

    public void setPrime(Long prime) {
        this.prime = prime;
    }

    public Long getHeuresuplemaentaire() {
        return heuresuplemaentaire;
    }

    public void setHeuresuplemaentaire(Long heuresuplemaentaire) {
        this.heuresuplemaentaire = heuresuplemaentaire;
    }

    public Long getCongeeAnnuel() {
        return congeeAnnuel;
    }

    public void setCongeeAnnuel(Long congeeAnnuel) {
        this.congeeAnnuel = congeeAnnuel;
    }

    public Long getRetenueCNSS() {
        return retenueCNSS;
    }

    public void setRetenueCNSS(Long retenueCNSS) {
        this.retenueCNSS = retenueCNSS;
    }

    public Long getSalairebrut() {
        return salairebrut;
    }

    public void setSalairebrut(Long salairebrut) {
        this.salairebrut = salairebrut;
    }

    public Long getDeductions() {
        return deductions;
    }

    public void setDeductions(Long deductions) {
        this.deductions = deductions;
    }

    public Long getContributionsociale() {
        return contributionsociale;
    }

    public void setContributionsociale(Long contributionsociale) {
        this.contributionsociale = contributionsociale;
    }

    public Long getSalairenet() {
        return salairenet;
    }

    public void setSalairenet(Long salairenet) {
        this.salairenet = salairenet;
    }

    public Long getAvance() {
        return avance;
    }

    public void setAvance(Long avance) {
        this.avance = avance;
    }

    public Long getNetapayer() {
        return netapayer;
    }

    public void setNetapayer(Long netapayer) {
        this.netapayer = netapayer;
    }

    public String getModedepaimenet() {
        return modedepaimenet;
    }

    public void setModedepaimenet(String modedepaimenet) {
        this.modedepaimenet = modedepaimenet;
    }

    public Long getNumcompte() {
        return numcompte;
    }

    public void setNumcompte(Long numcompte) {
        this.numcompte = numcompte;
    }

    public String getIntitulebanque() {
        return intitulebanque;
    }

    public void setIntitulebanque(String intitulebanque) {
        this.intitulebanque = intitulebanque;
    }

    public Long getCongeprisparmois() {
        return congeprisparmois;
    }

    public void setCongeprisparmois(Long congeprisparmois) {
        this.congeprisparmois = congeprisparmois;
    }

    public Long getSoldeconge() {
        return soldeconge;
    }

    public void setSoldeconge(Long soldeconge) {
        this.soldeconge = soldeconge;
    }

    public Long getNombredheuredabsence() {
        return nombredheuredabsence;
    }

    public void setNombredheuredabsence(Long nombredheuredabsence) {
        this.nombredheuredabsence = nombredheuredabsence;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
