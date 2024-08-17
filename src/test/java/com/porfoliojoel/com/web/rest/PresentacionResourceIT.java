package com.porfoliojoel.com.web.rest;

import static com.porfoliojoel.com.domain.PresentacionAsserts.*;
import static com.porfoliojoel.com.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porfoliojoel.com.IntegrationTest;
import com.porfoliojoel.com.domain.Presentacion;
import com.porfoliojoel.com.repository.PresentacionRepository;
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
 * Integration tests for the {@link PresentacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresentacionResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/presentacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PresentacionRepository presentacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresentacionMockMvc;

    private Presentacion presentacion;

    private Presentacion insertedPresentacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentacion createEntity(EntityManager em) {
        Presentacion presentacion = new Presentacion().descripcion(DEFAULT_DESCRIPCION);
        return presentacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presentacion createUpdatedEntity(EntityManager em) {
        Presentacion presentacion = new Presentacion().descripcion(UPDATED_DESCRIPCION);
        return presentacion;
    }

    @BeforeEach
    public void initTest() {
        presentacion = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPresentacion != null) {
            presentacionRepository.delete(insertedPresentacion);
            insertedPresentacion = null;
        }
    }

    @Test
    @Transactional
    void createPresentacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Presentacion
        var returnedPresentacion = om.readValue(
            restPresentacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(presentacion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Presentacion.class
        );

        // Validate the Presentacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPresentacionUpdatableFieldsEquals(returnedPresentacion, getPersistedPresentacion(returnedPresentacion));

        insertedPresentacion = returnedPresentacion;
    }

    @Test
    @Transactional
    void createPresentacionWithExistingId() throws Exception {
        // Create the Presentacion with an existing ID
        presentacion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresentacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(presentacion)))
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPresentacions() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        // Get all the presentacionList
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presentacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getPresentacion() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        // Get the presentacion
        restPresentacionMockMvc
            .perform(get(ENTITY_API_URL_ID, presentacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presentacion.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingPresentacion() throws Exception {
        // Get the presentacion
        restPresentacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPresentacion() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentacion
        Presentacion updatedPresentacion = presentacionRepository.findById(presentacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPresentacion are not directly saved in db
        em.detach(updatedPresentacion);
        updatedPresentacion.descripcion(UPDATED_DESCRIPCION);

        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPresentacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPresentacion))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPresentacionToMatchAllProperties(updatedPresentacion);
    }

    @Test
    @Transactional
    void putNonExistingPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presentacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(presentacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(presentacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(presentacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresentacionWithPatch() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentacion using partial update
        Presentacion partialUpdatedPresentacion = new Presentacion();
        partialUpdatedPresentacion.setId(presentacion.getId());

        partialUpdatedPresentacion.descripcion(UPDATED_DESCRIPCION);

        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPresentacion))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPresentacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPresentacion, presentacion),
            getPersistedPresentacion(presentacion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePresentacionWithPatch() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the presentacion using partial update
        Presentacion partialUpdatedPresentacion = new Presentacion();
        partialUpdatedPresentacion.setId(presentacion.getId());

        partialUpdatedPresentacion.descripcion(UPDATED_DESCRIPCION);

        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresentacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPresentacion))
            )
            .andExpect(status().isOk());

        // Validate the Presentacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPresentacionUpdatableFieldsEquals(partialUpdatedPresentacion, getPersistedPresentacion(partialUpdatedPresentacion));
    }

    @Test
    @Transactional
    void patchNonExistingPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presentacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(presentacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(presentacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresentacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        presentacion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresentacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(presentacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presentacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresentacion() throws Exception {
        // Initialize the database
        insertedPresentacion = presentacionRepository.saveAndFlush(presentacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the presentacion
        restPresentacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, presentacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return presentacionRepository.count();
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

    protected Presentacion getPersistedPresentacion(Presentacion presentacion) {
        return presentacionRepository.findById(presentacion.getId()).orElseThrow();
    }

    protected void assertPersistedPresentacionToMatchAllProperties(Presentacion expectedPresentacion) {
        assertPresentacionAllPropertiesEquals(expectedPresentacion, getPersistedPresentacion(expectedPresentacion));
    }

    protected void assertPersistedPresentacionToMatchUpdatableProperties(Presentacion expectedPresentacion) {
        assertPresentacionAllUpdatablePropertiesEquals(expectedPresentacion, getPersistedPresentacion(expectedPresentacion));
    }
}
