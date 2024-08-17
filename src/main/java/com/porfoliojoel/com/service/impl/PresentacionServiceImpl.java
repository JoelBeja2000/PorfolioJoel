package com.porfoliojoel.com.service.impl;

import com.porfoliojoel.com.domain.Presentacion;
import com.porfoliojoel.com.repository.PresentacionRepository;
import com.porfoliojoel.com.service.PresentacionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.porfoliojoel.com.domain.Presentacion}.
 */
@Service
@Transactional
public class PresentacionServiceImpl implements PresentacionService {

    private static final Logger log = LoggerFactory.getLogger(PresentacionServiceImpl.class);

    private final PresentacionRepository presentacionRepository;

    public PresentacionServiceImpl(PresentacionRepository presentacionRepository) {
        this.presentacionRepository = presentacionRepository;
    }

    @Override
    public Presentacion save(Presentacion presentacion) {
        log.debug("Request to save Presentacion : {}", presentacion);
        return presentacionRepository.save(presentacion);
    }

    @Override
    public Presentacion update(Presentacion presentacion) {
        log.debug("Request to update Presentacion : {}", presentacion);
        return presentacionRepository.save(presentacion);
    }

    @Override
    public Optional<Presentacion> partialUpdate(Presentacion presentacion) {
        log.debug("Request to partially update Presentacion : {}", presentacion);

        return presentacionRepository
            .findById(presentacion.getId())
            .map(existingPresentacion -> {
                if (presentacion.getDescripcion() != null) {
                    existingPresentacion.setDescripcion(presentacion.getDescripcion());
                }

                return existingPresentacion;
            })
            .map(presentacionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Presentacion> findAll(Pageable pageable) {
        log.debug("Request to get all Presentacions");
        return presentacionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Presentacion> findOne(Long id) {
        log.debug("Request to get Presentacion : {}", id);
        return presentacionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Presentacion : {}", id);
        presentacionRepository.deleteById(id);
    }
}
