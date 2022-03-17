package com.projet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prestataire.
 */
@Entity
@Table(name = "prestataire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prestataire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_pres")
    private String nomPres;

    @Column(name = "nom_cont")
    private String nomCont;

    @Column(name = "prenom_cont")
    private String prenomCont;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "prestataire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestataire", "fichePresences" }, allowSetters = true)
    private Set<Consultant> consultants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prestataire id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomPres() {
        return this.nomPres;
    }

    public Prestataire nomPres(String nomPres) {
        this.nomPres = nomPres;
        return this;
    }

    public void setNomPres(String nomPres) {
        this.nomPres = nomPres;
    }

    public String getNomCont() {
        return this.nomCont;
    }

    public Prestataire nomCont(String nomCont) {
        this.nomCont = nomCont;
        return this;
    }

    public void setNomCont(String nomCont) {
        this.nomCont = nomCont;
    }

    public String getPrenomCont() {
        return this.prenomCont;
    }

    public Prestataire prenomCont(String prenomCont) {
        this.prenomCont = prenomCont;
        return this;
    }

    public void setPrenomCont(String prenomCont) {
        this.prenomCont = prenomCont;
    }

    public String getEmail() {
        return this.email;
    }

    public Prestataire email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Consultant> getConsultants() {
        return this.consultants;
    }

    public Prestataire consultants(Set<Consultant> consultants) {
        this.setConsultants(consultants);
        return this;
    }

    public Prestataire addConsultant(Consultant consultant) {
        this.consultants.add(consultant);
        consultant.setPrestataire(this);
        return this;
    }

    public Prestataire removeConsultant(Consultant consultant) {
        this.consultants.remove(consultant);
        consultant.setPrestataire(null);
        return this;
    }

    public void setConsultants(Set<Consultant> consultants) {
        if (this.consultants != null) {
            this.consultants.forEach(i -> i.setPrestataire(null));
        }
        if (consultants != null) {
            consultants.forEach(i -> i.setPrestataire(this));
        }
        this.consultants = consultants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestataire)) {
            return false;
        }
        return id != null && id.equals(((Prestataire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestataire{" +
            "id=" + getId() +
            ", nomPres='" + getNomPres() + "'" +
            ", nomCont='" + getNomCont() + "'" +
            ", prenomCont='" + getPrenomCont() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
