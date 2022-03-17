package com.projet.service;

import com.projet.domain.Consultant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Consultant}.
 */
public interface ConsultantService {
    /**
     * Save a consultant.
     *
     * @param consultant the entity to save.
     * @return the persisted entity.
     */
    Consultant save(Consultant consultant);

    /**
     * Partially updates a consultant.
     *
     * @param consultant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Consultant> partialUpdate(Consultant consultant);

    /**
     * Get all the consultants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Consultant> findAll(Pageable pageable);

    /**
     * Get the "id" consultant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Consultant> findOne(Long id);

    /**
     * Delete the "id" consultant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
