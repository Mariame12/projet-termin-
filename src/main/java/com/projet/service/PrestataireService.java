package com.projet.service;

import com.projet.domain.Prestataire;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Prestataire}.
 */
public interface PrestataireService {
    /**
     * Save a prestataire.
     *
     * @param prestataire the entity to save.
     * @return the persisted entity.
     */
    Prestataire save(Prestataire prestataire);

    /**
     * Partially updates a prestataire.
     *
     * @param prestataire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Prestataire> partialUpdate(Prestataire prestataire);

    /**
     * Get all the prestataires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Prestataire> findAll(Pageable pageable);

    /**
     * Get the "id" prestataire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Prestataire> findOne(Long id);

    /**
     * Delete the "id" prestataire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
