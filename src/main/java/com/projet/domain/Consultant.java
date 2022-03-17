package com.projet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consultant.
 */
@Entity
@Table(name = "consultant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Consultant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "fonction")
    private String fonction;

    @ManyToOne
    @JsonIgnoreProperties(value = { "consultants" }, allowSetters = true)
    private Prestataire prestataire;

    @OneToMany(mappedBy = "consultant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consultant" }, allowSetters = true)
    private Set<FichePresence> fichePresences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultant id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Consultant nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Consultant prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getFonction() {
        return this.fonction;
    }

    public Consultant fonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Prestataire getPrestataire() {
        return this.prestataire;
    }

    public Consultant prestataire(Prestataire prestataire) {
        this.setPrestataire(prestataire);
        return this;
    }

    public void setPrestataire(Prestataire prestataire) {
        this.prestataire = prestataire;
    }

    public Set<FichePresence> getFichePresences() {
        return this.fichePresences;
    }

    public Consultant fichePresences(Set<FichePresence> fichePresences) {
        this.setFichePresences(fichePresences);
        return this;
    }

    public Consultant addFichePresence(FichePresence fichePresence) {
        this.fichePresences.add(fichePresence);
        fichePresence.setConsultant(this);
        return this;
    }

    public Consultant removeFichePresence(FichePresence fichePresence) {
        this.fichePresences.remove(fichePresence);
        fichePresence.setConsultant(null);
        return this;
    }

    public void setFichePresences(Set<FichePresence> fichePresences) {
        if (this.fichePresences != null) {
            this.fichePresences.forEach(i -> i.setConsultant(null));
        }
        if (fichePresences != null) {
            fichePresences.forEach(i -> i.setConsultant(this));
        }
        this.fichePresences = fichePresences;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultant)) {
            return false;
        }
        return id != null && id.equals(((Consultant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
    	return getNom() +" "+getPrenom();
    	}
}
