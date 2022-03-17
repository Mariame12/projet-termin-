package com.projet.repository;

import com.projet.domain.Consultant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Consultant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long>, JpaSpecificationExecutor<Consultant> {}
