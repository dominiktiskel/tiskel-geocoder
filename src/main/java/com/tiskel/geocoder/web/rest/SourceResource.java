package com.tiskel.geocoder.web.rest;

import com.tiskel.geocoder.repository.SourceRepository;
import com.tiskel.geocoder.service.SourceService;
import com.tiskel.geocoder.service.dto.SourceDTO;
import com.tiskel.geocoder.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tiskel.geocoder.domain.Source}.
 */
@RestController
@RequestMapping("/api")
public class SourceResource {

    private final Logger log = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "source";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceService sourceService;

    private final SourceRepository sourceRepository;

    public SourceResource(SourceService sourceService, SourceRepository sourceRepository) {
        this.sourceService = sourceService;
        this.sourceRepository = sourceRepository;
    }

    /**
     * {@code POST  /sources} : Create a new source.
     *
     * @param sourceDTO the sourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceDTO, or with status {@code 400 (Bad Request)} if the source has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources")
    public ResponseEntity<SourceDTO> createSource(@Valid @RequestBody SourceDTO sourceDTO) throws URISyntaxException {
        log.debug("REST request to save Source : {}", sourceDTO);
        if (sourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceDTO result = sourceService.save(sourceDTO);
        return ResponseEntity
            .created(new URI("/api/sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources/:id} : Updates an existing source.
     *
     * @param id the id of the sourceDTO to save.
     * @param sourceDTO the sourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceDTO,
     * or with status {@code 400 (Bad Request)} if the sourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources/{id}")
    public ResponseEntity<SourceDTO> updateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceDTO sourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Source : {}, {}", id, sourceDTO);
        if (sourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SourceDTO result = sourceService.save(sourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sources/:id} : Partial updates given fields of an existing source, field will ignore if it is null
     *
     * @param id the id of the sourceDTO to save.
     * @param sourceDTO the sourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceDTO,
     * or with status {@code 400 (Bad Request)} if the sourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceDTO> partialUpdateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceDTO sourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Source partially : {}, {}", id, sourceDTO);
        if (sourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceDTO> result = sourceService.partialUpdate(sourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("/sources")
    public List<SourceDTO> getAllSources() {
        log.debug("REST request to get all Sources");
        return sourceService.findAll();
    }

    /**
     * {@code GET  /sources/:id} : get the "id" source.
     *
     * @param id the id of the sourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources/{id}")
    public ResponseEntity<SourceDTO> getSource(@PathVariable Long id) {
        log.debug("REST request to get Source : {}", id);
        Optional<SourceDTO> sourceDTO = sourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceDTO);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" source.
     *
     * @param id the id of the sourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        log.debug("REST request to delete Source : {}", id);
        sourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
