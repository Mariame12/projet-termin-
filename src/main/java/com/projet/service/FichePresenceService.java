package com.projet.service;

import com.projet.domain.FichePresence;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link FichePresence}.
 */
public interface FichePresenceService {
    /**
     * Save a fichePresence.
     *
     * @param fichePresence the entity to save.
     * @return the persisted entity.
     */
    FichePresence save(FichePresence fichePresence);

    /**
     * Partially updates a fichePresence.
     *
     * @param fichePresence the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FichePresence> partialUpdate(FichePresence fichePresence);

    /**
     * Get all the fichePresences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FichePresence> findAll(Pageable pageable);

    /**
     * Get the "id" fichePresence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FichePresence> findOne(Long id);

    /**
     * Delete the "id" fichePresence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
