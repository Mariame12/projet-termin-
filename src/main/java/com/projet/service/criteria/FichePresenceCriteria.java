package com.projet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.projet.domain.FichePresence} entity. This class is used
 * in {@link com.projet.web.rest.FichePresenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiche-presences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FichePresenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter activites;

    private StringFilter heuredebut;

    private StringFilter heurefin;

    private LocalDateFilter date;

    private LongFilter consultantId;

    public FichePresenceCriteria() {}

    public FichePresenceCriteria(FichePresenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.activites = other.activites == null ? null : other.activites.copy();
        this.heuredebut = other.heuredebut == null ? null : other.heuredebut.copy();
        this.heurefin = other.heurefin == null ? null : other.heurefin.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.consultantId = other.consultantId == null ? null : other.consultantId.copy();
    }

    @Override
    public FichePresenceCriteria copy() {
        return new FichePresenceCriteria(this);
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

    public StringFilter getActivites() {
        return activites;
    }

    public StringFilter activites() {
        if (activites == null) {
            activites = new StringFilter();
        }
        return activites;
    }

    public void setActivites(StringFilter activites) {
        this.activites = activites;
    }

    public StringFilter getHeuredebut() {
        return heuredebut;
    }

    public StringFilter heuredebut() {
        if (heuredebut == null) {
            heuredebut = new StringFilter();
        }
        return heuredebut;
    }

    public void setHeuredebut(StringFilter heuredebut) {
        this.heuredebut = heuredebut;
    }

    public StringFilter getHeurefin() {
        return heurefin;
    }

    public StringFilter heurefin() {
        if (heurefin == null) {
            heurefin = new StringFilter();
        }
        return heurefin;
    }

    public void setHeurefin(StringFilter heurefin) {
        this.heurefin = heurefin;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
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
        final FichePresenceCriteria that = (FichePresenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(activites, that.activites) &&
            Objects.equals(heuredebut, that.heuredebut) &&
            Objects.equals(heurefin, that.heurefin) &&
            Objects.equals(date, that.date) &&
            Objects.equals(consultantId, that.consultantId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activites, heuredebut, heurefin, date, consultantId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichePresenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (activites != null ? "activites=" + activites + ", " : "") +
            (heuredebut != null ? "heuredebut=" + heuredebut + ", " : "") +
            (heurefin != null ? "heurefin=" + heurefin + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (consultantId != null ? "consultantId=" + consultantId + ", " : "") +
            "}";
    }
}
