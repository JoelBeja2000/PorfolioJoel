package com.porfoliojoel.com.web.rest;

import static com.porfoliojoel.com.domain.PersonaAsserts.*;
import static com.porfoliojoel.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfoliojoel.com.IntegrationTest;
import com.porfoliojoel.com.domain.Persona;
import com.porfoliojoel.com.repository.PersonaRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonaMockMvc;

    private Persona persona;

    private Persona insertedPersona;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createEntity(EntityManager em) {
        Persona persona = new Persona().nombre(DEFAULT_NOMBRE).apellido(DEFAULT_APELLIDO).email(DEFAULT_EMAIL).telefono(DEFAULT_TELEFONO);
        return persona;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Persona createUpdatedEntity(EntityManager em) {
        Persona persona = new Persona().nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO);
        return persona;
    }

    @BeforeEach
    public void initTest() {
        persona = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPersona != null) {
            personaRepository.delete(insertedPersona);
            insertedPersona = null;
        }
    }

    @Test
    @Transactional
    void createPersona() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Persona
        var returnedPersona = om.readValue(
            restPersonaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(persona)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Persona.class
        );

        // Validate the Persona in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPersonaUpdatableFieldsEquals(returnedPersona, getPersistedPersona(returnedPersona));

        insertedPersona = returnedPersona;
    }

    @Test
    @Transactional
    void createPersonaWithExistingId() throws Exception {
        // Create the Persona with an existing ID
        persona.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(persona)))
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonas() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get all the personaList
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(persona.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getPersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        // Get the persona
        restPersonaMockMvc
            .perform(get(ENTITY_API_URL_ID, persona.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(persona.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingPersona() throws Exception {
        // Get the persona
        restPersonaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona
        Persona updatedPersona = personaRepository.findById(persona.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersona are not directly saved in db
        em.detach(updatedPersona);
        updatedPersona.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO);

        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersona.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPersonaToMatchAllProperties(updatedPersona);
    }

    @Test
    @Transactional
    void putNonExistingPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(put(ENTITY_API_URL_ID, persona.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(persona)))
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(persona))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(persona)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona.nombre(UPDATED_NOMBRE).email(UPDATED_EMAIL);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPersona, persona), getPersistedPersona(persona));
    }

    @Test
    @Transactional
    void fullUpdatePersonaWithPatch() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the persona using partial update
        Persona partialUpdatedPersona = new Persona();
        partialUpdatedPersona.setId(persona.getId());

        partialUpdatedPersona.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).email(UPDATED_EMAIL).telefono(UPDATED_TELEFONO);

        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersona.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPersona))
            )
            .andExpect(status().isOk());

        // Validate the Persona in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersonaUpdatableFieldsEquals(partialUpdatedPersona, getPersistedPersona(partialUpdatedPersona));
    }

    @Test
    @Transactional
    void patchNonExistingPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, persona.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(persona))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(persona))
            )
            .andExpect(status().isBadRequest());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersona() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        persona.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(persona)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Persona in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersona() throws Exception {
        // Initialize the database
        insertedPersona = personaRepository.saveAndFlush(persona);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the persona
        restPersonaMockMvc
            .perform(delete(ENTITY_API_URL_ID, persona.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return personaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Persona getPersistedPersona(Persona persona) {
        return personaRepository.findById(persona.getId()).orElseThrow();
    }

    protected void assertPersistedPersonaToMatchAllProperties(Persona expectedPersona) {
        assertPersonaAllPropertiesEquals(expectedPersona, getPersistedPersona(expectedPersona));
    }

    protected void assertPersistedPersonaToMatchUpdatableProperties(Persona expectedPersona) {
        assertPersonaAllUpdatablePropertiesEquals(expectedPersona, getPersistedPersona(expectedPersona));
    }
}
