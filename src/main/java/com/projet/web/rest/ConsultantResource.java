package com.projet.web.rest;

import com.projet.domain.Consultant;
import com.projet.repository.ConsultantRepository;
import com.projet.service.ConsultantQueryService;
import com.projet.service.ConsultantService;
import com.projet.service.criteria.ConsultantCriteria;
import com.projet.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.projet.domain.Consultant}.
 */
@RestController
@RequestMapping("/api")
public class ConsultantResource {

    private final Logger log = LoggerFactory.getLogger(ConsultantResource.class);

    private static final String ENTITY_NAME = "consultant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultantService consultantService;

    private final ConsultantRepository consultantRepository;

    private final ConsultantQueryService consultantQueryService;

    public ConsultantResource(
        ConsultantService consultantService,
        ConsultantRepository consultantRepository,
        ConsultantQueryService consultantQueryService
    ) {
        this.consultantService = consultantService;
        this.consultantRepository = consultantRepository;
        this.consultantQueryService = consultantQueryService;
    }

    /**
     * {@code POST  /consultants} : Create a new consultant.
     *
     * @param consultant the consultant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultant, or with status {@code 400 (Bad Request)} if the consultant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultants")
    public ResponseEntity<Consultant> createConsultant(@RequestBody Consultant consultant) throws URISyntaxException {
        log.debug("REST request to save Consultant : {}", consultant);
        if (consultant.getId() != null) {
            throw new BadRequestAlertException("A new consultant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultant result = consultantService.save(consultant);
        return ResponseEntity
            .created(new URI("/api/consultants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultants/:id} : Updates an existing consultant.
     *
     * @param id the id of the consultant to save.
     * @param consultant the consultant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultant,
     * or with status {@code 400 (Bad Request)} if the consultant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultants/{id}")
    public ResponseEntity<Consultant> updateConsultant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Consultant consultant
    ) throws URISyntaxException {
        log.debug("REST request to update Consultant : {}, {}", id, consultant);
        if (consultant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Consultant result = consultantService.save(consultant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultants/:id} : Partial updates given fields of an existing consultant, field will ignore if it is null
     *
     * @param id the id of the consultant to save.
     * @param consultant the consultant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultant,
     * or with status {@code 400 (Bad Request)} if the consultant is not valid,
     * or with status {@code 404 (Not Found)} if the consultant is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consultants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Consultant> partialUpdateConsultant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Consultant consultant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultant partially : {}, {}", id, consultant);
        if (consultant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Consultant> result = consultantService.partialUpdate(consultant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultant.getId().toString())
        );
    }

    /**
     * {@code GET  /consultants} : get all the consultants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultants in body.
     */
    @GetMapping("/consultants")
    public ResponseEntity<List<Consultant>> getAllConsultants(ConsultantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Consultants by criteria: {}", criteria);
        Page<Consultant> page = consultantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consultants/count} : count all the consultants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/consultants/count")
    public ResponseEntity<Long> countConsultants(ConsultantCriteria criteria) {
        log.debug("REST request to count Consultants by criteria: {}", criteria);
        return ResponseEntity.ok().body(consultantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consultants/:id} : get the "id" consultant.
     *
     * @param id the id of the consultant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultants/{id}")
    public ResponseEntity<Consultant> getConsultant(@PathVariable Long id) {
        log.debug("REST request to get Consultant : {}", id);
        Optional<Consultant> consultant = consultantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultant);
    }

    /**
     * {@code DELETE  /consultants/:id} : delete the "id" consultant.
     *
     * @param id the id of the consultant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultants/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        log.debug("REST request to delete Consultant : {}", id);
        consultantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
