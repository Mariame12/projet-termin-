package com.projet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.IntegrationTest;
import com.projet.domain.Consultant;
import com.projet.domain.FichePresence;
import com.projet.domain.Prestataire;
import com.projet.repository.ConsultantRepository;
import com.projet.service.criteria.ConsultantCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConsultantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultantMockMvc;

    private Consultant consultant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultant createEntity(EntityManager em) {
        Consultant consultant = new Consultant().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM).fonction(DEFAULT_FONCTION);
        return consultant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultant createUpdatedEntity(EntityManager em) {
        Consultant consultant = new Consultant().nom(UPDATED_NOM).prenom(UPDATED_PRENOM).fonction(UPDATED_FONCTION);
        return consultant;
    }

    @BeforeEach
    public void initTest() {
        consultant = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultant() throws Exception {
        int databaseSizeBeforeCreate = consultantRepository.findAll().size();
        // Create the Consultant
        restConsultantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isCreated());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeCreate + 1);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConsultant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testConsultant.getFonction()).isEqualTo(DEFAULT_FONCTION);
    }

    @Test
    @Transactional
    void createConsultantWithExistingId() throws Exception {
        // Create the Consultant with an existing ID
        consultant.setId(1L);

        int databaseSizeBeforeCreate = consultantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultants() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)));
    }

    @Test
    @Transactional
    void getConsultant() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get the consultant
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL_ID, consultant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION));
    }

    @Test
    @Transactional
    void getConsultantsByIdFiltering() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        Long id = consultant.getId();

        defaultConsultantShouldBeFound("id.equals=" + id);
        defaultConsultantShouldNotBeFound("id.notEquals=" + id);

        defaultConsultantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsultantShouldNotBeFound("id.greaterThan=" + id);

        defaultConsultantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsultantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsultantsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom equals to DEFAULT_NOM
        defaultConsultantShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the consultantList where nom equals to UPDATED_NOM
        defaultConsultantShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom not equals to DEFAULT_NOM
        defaultConsultantShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the consultantList where nom not equals to UPDATED_NOM
        defaultConsultantShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultConsultantShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the consultantList where nom equals to UPDATED_NOM
        defaultConsultantShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom is not null
        defaultConsultantShouldBeFound("nom.specified=true");

        // Get all the consultantList where nom is null
        defaultConsultantShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultantsByNomContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom contains DEFAULT_NOM
        defaultConsultantShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the consultantList where nom contains UPDATED_NOM
        defaultConsultantShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where nom does not contain DEFAULT_NOM
        defaultConsultantShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the consultantList where nom does not contain UPDATED_NOM
        defaultConsultantShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom equals to DEFAULT_PRENOM
        defaultConsultantShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the consultantList where prenom equals to UPDATED_PRENOM
        defaultConsultantShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom not equals to DEFAULT_PRENOM
        defaultConsultantShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the consultantList where prenom not equals to UPDATED_PRENOM
        defaultConsultantShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultConsultantShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the consultantList where prenom equals to UPDATED_PRENOM
        defaultConsultantShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom is not null
        defaultConsultantShouldBeFound("prenom.specified=true");

        // Get all the consultantList where prenom is null
        defaultConsultantShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom contains DEFAULT_PRENOM
        defaultConsultantShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the consultantList where prenom contains UPDATED_PRENOM
        defaultConsultantShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where prenom does not contain DEFAULT_PRENOM
        defaultConsultantShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the consultantList where prenom does not contain UPDATED_PRENOM
        defaultConsultantShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction equals to DEFAULT_FONCTION
        defaultConsultantShouldBeFound("fonction.equals=" + DEFAULT_FONCTION);

        // Get all the consultantList where fonction equals to UPDATED_FONCTION
        defaultConsultantShouldNotBeFound("fonction.equals=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction not equals to DEFAULT_FONCTION
        defaultConsultantShouldNotBeFound("fonction.notEquals=" + DEFAULT_FONCTION);

        // Get all the consultantList where fonction not equals to UPDATED_FONCTION
        defaultConsultantShouldBeFound("fonction.notEquals=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction in DEFAULT_FONCTION or UPDATED_FONCTION
        defaultConsultantShouldBeFound("fonction.in=" + DEFAULT_FONCTION + "," + UPDATED_FONCTION);

        // Get all the consultantList where fonction equals to UPDATED_FONCTION
        defaultConsultantShouldNotBeFound("fonction.in=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction is not null
        defaultConsultantShouldBeFound("fonction.specified=true");

        // Get all the consultantList where fonction is null
        defaultConsultantShouldNotBeFound("fonction.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction contains DEFAULT_FONCTION
        defaultConsultantShouldBeFound("fonction.contains=" + DEFAULT_FONCTION);

        // Get all the consultantList where fonction contains UPDATED_FONCTION
        defaultConsultantShouldNotBeFound("fonction.contains=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllConsultantsByFonctionNotContainsSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        // Get all the consultantList where fonction does not contain DEFAULT_FONCTION
        defaultConsultantShouldNotBeFound("fonction.doesNotContain=" + DEFAULT_FONCTION);

        // Get all the consultantList where fonction does not contain UPDATED_FONCTION
        defaultConsultantShouldBeFound("fonction.doesNotContain=" + UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void getAllConsultantsByPrestataireIsEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);
        Prestataire prestataire = PrestataireResourceIT.createEntity(em);
        em.persist(prestataire);
        em.flush();
        consultant.setPrestataire(prestataire);
        consultantRepository.saveAndFlush(consultant);
        Long prestataireId = prestataire.getId();

        // Get all the consultantList where prestataire equals to prestataireId
        defaultConsultantShouldBeFound("prestataireId.equals=" + prestataireId);

        // Get all the consultantList where prestataire equals to (prestataireId + 1)
        defaultConsultantShouldNotBeFound("prestataireId.equals=" + (prestataireId + 1));
    }

    @Test
    @Transactional
    void getAllConsultantsByFichePresenceIsEqualToSomething() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);
        FichePresence fichePresence = FichePresenceResourceIT.createEntity(em);
        em.persist(fichePresence);
        em.flush();
        consultant.addFichePresence(fichePresence);
        consultantRepository.saveAndFlush(consultant);
        Long fichePresenceId = fichePresence.getId();

        // Get all the consultantList where fichePresence equals to fichePresenceId
        defaultConsultantShouldBeFound("fichePresenceId.equals=" + fichePresenceId);

        // Get all the consultantList where fichePresence equals to (fichePresenceId + 1)
        defaultConsultantShouldNotBeFound("fichePresenceId.equals=" + (fichePresenceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsultantShouldBeFound(String filter) throws Exception {
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)));

        // Check, that the count call also returns 1
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsultantShouldNotBeFound(String filter) throws Exception {
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsultantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsultant() throws Exception {
        // Get the consultant
        restConsultantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsultant() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();

        // Update the consultant
        Consultant updatedConsultant = consultantRepository.findById(consultant.getId()).get();
        // Disconnect from session so that the updates on updatedConsultant are not directly saved in db
        em.detach(updatedConsultant);
        updatedConsultant.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).fonction(UPDATED_FONCTION);

        restConsultantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsultant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConsultant))
            )
            .andExpect(status().isOk());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConsultant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testConsultant.getFonction()).isEqualTo(UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void putNonExistingConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultantWithPatch() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();

        // Update the consultant using partial update
        Consultant partialUpdatedConsultant = new Consultant();
        partialUpdatedConsultant.setId(consultant.getId());

        partialUpdatedConsultant.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).fonction(UPDATED_FONCTION);

        restConsultantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultant))
            )
            .andExpect(status().isOk());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConsultant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testConsultant.getFonction()).isEqualTo(UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void fullUpdateConsultantWithPatch() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();

        // Update the consultant using partial update
        Consultant partialUpdatedConsultant = new Consultant();
        partialUpdatedConsultant.setId(consultant.getId());

        partialUpdatedConsultant.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).fonction(UPDATED_FONCTION);

        restConsultantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultant))
            )
            .andExpect(status().isOk());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
        Consultant testConsultant = consultantList.get(consultantList.size() - 1);
        assertThat(testConsultant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConsultant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testConsultant.getFonction()).isEqualTo(UPDATED_FONCTION);
    }

    @Test
    @Transactional
    void patchNonExistingConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultant() throws Exception {
        int databaseSizeBeforeUpdate = consultantRepository.findAll().size();
        consultant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consultant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultant in the database
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultant() throws Exception {
        // Initialize the database
        consultantRepository.saveAndFlush(consultant);

        int databaseSizeBeforeDelete = consultantRepository.findAll().size();

        // Delete the consultant
        restConsultantMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultant> consultantList = consultantRepository.findAll();
        assertThat(consultantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
