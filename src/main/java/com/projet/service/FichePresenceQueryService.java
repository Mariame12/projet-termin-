package com.projet.service;

import com.projet.domain.*; // for static metamodels
import com.projet.domain.FichePresence;
import com.projet.repository.FichePresenceRepository;
import com.projet.service.criteria.FichePresenceCriteria;
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
 * Service for executing complex queries for {@link FichePresence} entities in the database.
 * The main input is a {@link FichePresenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FichePresence} or a {@link Page} of {@link FichePresence} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FichePresenceQueryService extends QueryService<FichePresence> {

    private final Logger log = LoggerFactory.getLogger(FichePresenceQueryService.class);

    private final FichePresenceRepository fichePresenceRepository;

    public FichePresenceQueryService(FichePresenceRepository fichePresenceRepository) {
        this.fichePresenceRepository = fichePresenceRepository;
    }

    /**
     * Return a {@link List} of {@link FichePresence} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FichePresence> findByCriteria(FichePresenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FichePresence} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FichePresence> findByCriteria(FichePresenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FichePresenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FichePresence> specification = createSpecification(criteria);
        return fichePresenceRepository.count(specification);
    }

    /**
     * Function to convert {@link FichePresenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FichePresence> createSpecification(FichePresenceCriteria criteria) {
        Specification<FichePresence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FichePresence_.id));
            }
            if (criteria.getActivites() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivites(), FichePresence_.activites));
            }
            if (criteria.getHeuredebut() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeuredebut(), FichePresence_.heuredebut));
            }
            if (criteria.getHeurefin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeurefin(), FichePresence_.heurefin));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), FichePresence_.date));
            }
            if (criteria.getConsultantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultantId(),
                            root -> root.join(FichePresence_.consultant, JoinType.LEFT).get(Consultant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
