package com.projet.service;

import com.projet.domain.*; // for static metamodels
import com.projet.domain.Prestataire;
import com.projet.repository.PrestataireRepository;
import com.projet.service.criteria.PrestataireCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Prestataire} entities in the database.
 * The main input is a {@link PrestataireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Prestataire} or a {@link Page} of {@link Prestataire} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrestataireQueryService extends QueryService<Prestataire> {

    private final Logger log = LoggerFactory.getLogger(PrestataireQueryService.class);

    private final PrestataireRepository prestataireRepository;

    public PrestataireQueryService(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    /**
     * Return a {@link List} of {@link Prestataire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Prestataire> findByCriteria(PrestataireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prestataire> specification = createSpecification(criteria);
        return prestataireRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Prestataire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Prestataire> findByCriteria(PrestataireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prestataire> specification = createSpecification(criteria);
        return prestataireRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrestataireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Prestataire> specification = createSpecification(criteria);
        return prestataireRepository.count(specification);
    }

    /**
     * Function to convert {@link PrestataireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Prestataire> createSpecification(PrestataireCriteria criteria) {
        Specification<Prestataire> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Prestataire_.id));
            }
            if (criteria.getNomPres() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomPres(), Prestataire_.nomPres));
            }
            if (criteria.getNomCont() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomCont(), Prestataire_.nomCont));
            }
            if (criteria.getPrenomCont() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenomCont(), Prestataire_.prenomCont));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Prestataire_.email));
            }
            if (criteria.getConsultantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultantId(),
                            root -> root.join(Prestataire_.consultants, JoinType.LEFT).get(Consultant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
