package com.projet.service;

import com.projet.domain.*; // for static metamodels
import com.projet.domain.Consultant;
import com.projet.repository.ConsultantRepository;
import com.projet.service.criteria.ConsultantCriteria;
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
 * Service for executing complex queries for {@link Consultant} entities in the database.
 * The main input is a {@link ConsultantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Consultant} or a {@link Page} of {@link Consultant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsultantQueryService extends QueryService<Consultant> {

    private final Logger log = LoggerFactory.getLogger(ConsultantQueryService.class);

    private final ConsultantRepository consultantRepository;

    public ConsultantQueryService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    /**
     * Return a {@link List} of {@link Consultant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Consultant> findByCriteria(ConsultantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Consultant> specification = createSpecification(criteria);
        return consultantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Consultant} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Consultant> findByCriteria(ConsultantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Consultant> specification = createSpecification(criteria);
        return consultantRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsultantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Consultant> specification = createSpecification(criteria);
        return consultantRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsultantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Consultant> createSpecification(ConsultantCriteria criteria) {
        Specification<Consultant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Consultant_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Consultant_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Consultant_.prenom));
            }
            if (criteria.getFonction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFonction(), Consultant_.fonction));
            }
            if (criteria.getPrestataireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrestataireId(),
                            root -> root.join(Consultant_.prestataire, JoinType.LEFT).get(Prestataire_.id)
                        )
                    );
            }
            if (criteria.getFichePresenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFichePresenceId(),
                            root -> root.join(Consultant_.fichePresences, JoinType.LEFT).get(FichePresence_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
