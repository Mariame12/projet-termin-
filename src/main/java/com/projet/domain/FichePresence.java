package com.projet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FichePresence.
 */
@Entity
@Table(name = "fiche_presence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FichePresence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activites")
    private String activites;

    @Column(name = "heuredebut")
    private String heuredebut;

    @Lob
    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "heurefin")
    private String heurefin;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "prestataire", "fichePresences" }, allowSetters = true)
    private Consultant consultant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FichePresence id(Long id) {
        this.id = id;
        return this;
    }

    public String getActivites() {
        return this.activites;
    }

    public FichePresence activites(String activites) {
        this.activites = activites;
        return this;
    }

    public void setActivites(String activites) {
        this.activites = activites;
    }

    public String getHeuredebut() {
        return this.heuredebut;
    }

    public FichePresence heuredebut(String heuredebut) {
        this.heuredebut = heuredebut;
        return this;
    }

    public void setHeuredebut(String heuredebut) {
        this.heuredebut = heuredebut;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public FichePresence commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getHeurefin() {
        return this.heurefin;
    }

    public FichePresence heurefin(String heurefin) {
        this.heurefin = heurefin;
        return this;
    }

    public void setHeurefin(String heurefin) {
        this.heurefin = heurefin;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public FichePresence date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Consultant getConsultant() {
        return this.consultant;
    }

    public FichePresence consultant(Consultant consultant) {
        this.setConsultant(consultant);
        return this;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichePresence)) {
            return false;
        }
        return id != null && id.equals(((FichePresence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichePresence{" +
            "id=" + getId() +
            ", activites='" + getActivites() + "'" +
            ", heuredebut='" + getHeuredebut() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", heurefin='" + getHeurefin() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
