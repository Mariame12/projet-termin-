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
 * Criteria class for the {@link com.projet.domain.Prestataire} entity. This class is used
 * in {@link com.projet.web.rest.PrestataireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prestataires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrestataireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomPres;

    private StringFilter nomCont;

    private StringFilter prenomCont;

    private StringFilter email;

    private LongFilter consultantId;

    public PrestataireCriteria() {}

    public PrestataireCriteria(PrestataireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomPres = other.nomPres == null ? null : other.nomPres.copy();
        this.nomCont = other.nomCont == null ? null : other.nomCont.copy();
        this.prenomCont = other.prenomCont == null ? null : other.prenomCont.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.consultantId = other.consultantId == null ? null : other.consultantId.copy();
    }

    @Override
    public PrestataireCriteria copy() {
        return new PrestataireCriteria(this);
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

    public StringFilter getNomPres() {
        return nomPres;
    }

    public StringFilter nomPres() {
        if (nomPres == null) {
            nomPres = new StringFilter();
        }
        return nomPres;
    }

    public void setNomPres(StringFilter nomPres) {
        this.nomPres = nomPres;
    }

    public StringFilter getNomCont() {
        return nomCont;
    }

    public StringFilter nomCont() {
        if (nomCont == null) {
            nomCont = new StringFilter();
        }
        return nomCont;
    }

    public void setNomCont(StringFilter nomCont) {
        this.nomCont = nomCont;
    }

    public StringFilter getPrenomCont() {
        return prenomCont;
    }

    public StringFilter prenomCont() {
        if (prenomCont == null) {
            prenomCont = new StringFilter();
        }
        return prenomCont;
    }

    public void setPrenomCont(StringFilter prenomCont) {
        this.prenomCont = prenomCont;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getConsultantId() {
        return consultantId;
    }

    public LongFilter consultantId() {
        if (consultantId == null) {
            consultantId = new LongFilter();
        }
        return consultantId;
    }

    public void setConsultantId(LongFilter consultantId) {
        this.consultantId = consultantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrestataireCriteria that = (PrestataireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomPres, that.nomPres) &&
            Objects.equals(nomCont, that.nomCont) &&
            Objects.equals(prenomCont, that.prenomCont) &&
            Objects.equals(email, that.email) &&
            Objects.equals(consultantId, that.consultantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomPres, nomCont, prenomCont, email, consultantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestataireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nomPres != null ? "nomPres=" + nomPres + ", " : "") +
            (nomCont != null ? "nomCont=" + nomCont + ", " : "") +
            (prenomCont != null ? "prenomCont=" + prenomCont + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (consultantId != null ? "consultantId=" + consultantId + ", " : "") +
            "}";
    }
}
