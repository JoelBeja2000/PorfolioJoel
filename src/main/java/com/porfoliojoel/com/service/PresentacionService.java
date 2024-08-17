package com.porfoliojoel.com.service;

import com.porfoliojoel.com.domain.Presentacion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.porfoliojoel.com.domain.Presentacion}.
 */
public interface PresentacionService {
    /**
     * Save a presentacion.
     *
     * @param presentacion the entity to save.
     * @return the persisted entity.
     */
    Presentacion save(Presentacion presentacion);

    /**
     * Updates a presentacion.
     *
     * @param presentacion the entity to update.
     * @return the persisted entity.
     */
    Presentacion update(Presentacion presentacion);

    /**
     * Partially updates a presentacion.
     *
     * @param presentacion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Presentacion> partialUpdate(Presentacion presentacion);

    /**
     * Get all the presentacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Presentacion> findAll(Pageable pageable);

    /**
     * Get the "id" presentacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Presentacion> findOne(Long id);

    /**
     * Delete the "id" presentacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
