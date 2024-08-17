package com.porfoliojoel.com.web.rest;

import com.porfoliojoel.com.domain.Presentacion;
import com.porfoliojoel.com.repository.PresentacionRepository;
import com.porfoliojoel.com.service.PresentacionService;
import com.porfoliojoel.com.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.porfoliojoel.com.domain.Presentacion}.
 */
@RestController
@RequestMapping("/api/presentacions")
public class PresentacionResource {

    private static final Logger log = LoggerFactory.getLogger(PresentacionResource.class);

    private static final String ENTITY_NAME = "presentacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresentacionService presentacionService;

    private final PresentacionRepository presentacionRepository;

    public PresentacionResource(PresentacionService presentacionService, PresentacionRepository presentacionRepository) {
        this.presentacionService = presentacionService;
        this.presentacionRepository = presentacionRepository;
    }

    /**
     * {@code POST  /presentacions} : Create a new presentacion.
     *
     * @param presentacion the presentacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presentacion, or with status {@code 400 (Bad Request)} if the presentacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Presentacion> createPresentacion(@RequestBody Presentacion presentacion) throws URISyntaxException {
        log.debug("REST request to save Presentacion : {}", presentacion);
        if (presentacion.getId() != null) {
            throw new BadRequestAlertException("A new presentacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        presentacion = presentacionService.save(presentacion);
        return ResponseEntity.created(new URI("/api/presentacions/" + presentacion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, presentacion.getId().toString()))
            .body(presentacion);
    }

    /**
     * {@code PUT  /presentacions/:id} : Updates an existing presentacion.
     *
     * @param id the id of the presentacion to save.
     * @param presentacion the presentacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentacion,
     * or with status {@code 400 (Bad Request)} if the presentacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presentacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Presentacion> updatePresentacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Presentacion presentacion
    ) throws URISyntaxException {
        log.debug("REST request to update Presentacion : {}, {}", id, presentacion);
        if (presentacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        presentacion = presentacionService.update(presentacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentacion.getId().toString()))
            .body(presentacion);
    }

    /**
     * {@code PATCH  /presentacions/:id} : Partial updates given fields of an existing presentacion, field will ignore if it is null
     *
     * @param id the id of the presentacion to save.
     * @param presentacion the presentacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presentacion,
     * or with status {@code 400 (Bad Request)} if the presentacion is not valid,
     * or with status {@code 404 (Not Found)} if the presentacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the presentacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Presentacion> partialUpdatePresentacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Presentacion presentacion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Presentacion partially : {}, {}", id, presentacion);
        if (presentacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presentacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presentacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Presentacion> result = presentacionService.partialUpdate(presentacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presentacion.getId().toString())
        );
    }

    /**
     * {@code GET  /presentacions} : get all the presentacions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presentacions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Presentacion>> getAllPresentacions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Presentacions");
        Page<Presentacion> page = presentacionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /presentacions/:id} : get the "id" presentacion.
     *
     * @param id the id of the presentacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presentacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Presentacion> getPresentacion(@PathVariable("id") Long id) {
        log.debug("REST request to get Presentacion : {}", id);
        Optional<Presentacion> presentacion = presentacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presentacion);
    }

    /**
     * {@code DELETE  /presentacions/:id} : delete the "id" presentacion.
     *
     * @param id the id of the presentacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresentacion(@PathVariable("id") Long id) {
        log.debug("REST request to delete Presentacion : {}", id);
        presentacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
