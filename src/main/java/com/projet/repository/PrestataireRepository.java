package com.projet.repository;

import com.projet.domain.Prestataire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prestataire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, Long>, JpaSpecificationExecutor<Prestataire> {}
