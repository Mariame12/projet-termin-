package com.projet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.projet.domain.Consultant} entity. This class is used
 * in {@link com.projet.web.rest.ConsultantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consultants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConsultantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter fonction;

    private LongFilter prestataireId;

    private LongFilter fichePresenceId;

    public ConsultantCriteria() {}

    public ConsultantCriteria(ConsultantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.fonction = other.fonction == null ? null : other.fonction.copy();
        this.prestataireId = other.prestataireId == null ? null : other.prestataireId.copy();
        this.fichePresenceId = other.fichePresenceId == null ? null : other.fichePresenceId.copy();
    }

    @Override
    public ConsultantCriteria copy() {
        return new ConsultantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getFonction() {
        return fonction;
    }

    public StringFilter fonction() {
        if (fonction == null) {
            fonction = new StringFilter();
        }
        return fonction;
    }

    public void setFonction(StringFilter fonction) {
        this.fonction = fonction;
    }

    public LongFilter getPrestataireId() {
        return prestataireId;
    }

    public LongFilter prestataireId() {
        if (prestataireId == null) {
            prestataireId = new LongFilter();
        }
        return prestataireId;
    }

    public void setPrestataireId(LongFilter prestataireId) {
        this.prestataireId = prestataireId;
    }

    public LongFilter getFichePresenceId() {
        return fichePresenceId;
    }

    public LongFilter fichePresenceId() {
        if (fichePresenceId == null) {
            fichePresenceId = new LongFilter();
        }
        return fichePresenceId;
    }

    public void setFichePresenceId(LongFilter fichePresenceId) {
        this.fichePresenceId = fichePresenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConsultantCriteria that = (ConsultantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(fonction, that.fonction) &&
            Objects.equals(prestataireId, that.prestataireId) &&
            Objects.equals(fichePresenceId, that.fichePresenceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, fonction, prestataireId, fichePresenceId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultantCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (fonction != null ? "fonction=" + fonction + ", " : "") +
            (prestataireId != null ? "prestataireId=" + prestataireId + ", " : "") +
            (fichePresenceId != null ? "fichePresenceId=" + fichePresenceId + ", " : "") +
            "}";
    }
}
