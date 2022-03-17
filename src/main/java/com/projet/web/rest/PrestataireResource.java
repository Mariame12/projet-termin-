package com.projet.web.rest;

import com.projet.domain.Prestataire;
import com.projet.repository.PrestataireRepository;
import com.projet.service.PrestataireQueryService;
import com.projet.service.PrestataireService;
import com.projet.service.criteria.PrestataireCriteria;
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
 * REST controller for managing {@link com.projet.domain.Prestataire}.
 */
@RestController
@RequestMapping("/api")
public class PrestataireResource {

    private final Logger log = LoggerFactory.getLogger(PrestataireResource.class);

    private static final String ENTITY_NAME = "prestataire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestataireService prestataireService;

    private final PrestataireRepository prestataireRepository;

    private final PrestataireQueryService prestataireQueryService;

    public PrestataireResource(
        PrestataireService prestataireService,
        PrestataireRepository prestataireRepository,
        PrestataireQueryService prestataireQueryService
    ) {
        this.prestataireService = prestataireService;
        this.prestataireRepository = prestataireRepository;
        this.prestataireQueryService = prestataireQueryService;
    }

    /**
     * {@code POST  /prestataires} : Create a new prestataire.
     *
     * @param prestataire the prestataire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestataire, or with status {@code 400 (Bad Request)} if the prestataire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prestataires")
    public ResponseEntity<Prestataire> createPrestataire(@RequestBody Prestataire prestataire) throws URISyntaxException {
        log.debug("REST request to save Prestataire : {}", prestataire);
        if (prestataire.getId() != null) {
            throw new BadRequestAlertException("A new prestataire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prestataire result = prestataireService.save(prestataire);
        return ResponseEntity
            .created(new URI("/api/prestataires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prestataires/:id} : Updates an existing prestataire.
     *
     * @param id the id of the prestataire to save.
     * @param prestataire the prestataire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestataire,
     * or with status {@code 400 (Bad Request)} if the prestataire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestataire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prestataires/{id}")
    public ResponseEntity<Prestataire> updatePrestataire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prestataire prestataire
    ) throws URISyntaxException {
        log.debug("REST request to update Prestataire : {}, {}", id, prestataire);
        if (prestataire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestataire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestataireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prestataire result = prestataireService.save(prestataire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestataire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prestataires/:id} : Partial updates given fields of an existing prestataire, field will ignore if it is null
     *
     * @param id the id of the prestataire to save.
     * @param prestataire the prestataire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestataire,
     * or with status {@code 400 (Bad Request)} if the prestataire is not valid,
     * or with status {@code 404 (Not Found)} if the prestataire is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestataire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prestataires/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Prestataire> partialUpdatePrestataire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prestataire prestataire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prestataire partially : {}, {}", id, prestataire);
        if (prestataire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestataire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestataireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prestataire> result = prestataireService.partialUpdate(prestataire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestataire.getId().toString())
        );
    }

    /**
     * {@code GET  /prestataires} : get all the prestataires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestataires in body.
     */
    @GetMapping("/prestataires")
    public ResponseEntity<List<Prestataire>> getAllPrestataires(PrestataireCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Prestataires by criteria: {}", criteria);
        Page<Prestataire> page = prestataireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestataires/count} : count all the prestataires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prestataires/count")
    public ResponseEntity<Long> countPrestataires(PrestataireCriteria criteria) {
        log.debug("REST request to count Prestataires by criteria: {}", criteria);
        return ResponseEntity.ok().body(prestataireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prestataires/:id} : get the "id" prestataire.
     *
     * @param id the id of the prestataire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestataire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prestataires/{id}")
    public ResponseEntity<Prestataire> getPrestataire(@PathVariable Long id) {
        log.debug("REST request to get Prestataire : {}", id);
        Optional<Prestataire> prestataire = prestataireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestataire);
    }

    /**
     * {@code DELETE  /prestataires/:id} : delete the "id" prestataire.
     *
     * @param id the id of the prestataire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prestataires/{id}")
    public ResponseEntity<Void> deletePrestataire(@PathVariable Long id) {
        log.debug("REST request to delete Prestataire : {}", id);
        prestataireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
