package com.sip.store.entities;

import javax.persistence.*;
import java.util.Date;
@Entity
public class Reglement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String duree;
    private String montant;
    private String modalite;
    private String remarque;
    private Date date;
    @OneToOne
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getModalite() {
        return modalite;
    }

    public void setModalite(String modalite) {
        this.modalite = modalite;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
