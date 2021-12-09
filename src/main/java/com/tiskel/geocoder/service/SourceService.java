package com.tiskel.geocoder.service;

import com.tiskel.geocoder.service.dto.SourceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.tiskel.geocoder.domain.Source}.
 */
public interface SourceService {
    /**
     * Save a source.
     *
     * @param sourceDTO the entity to save.
     * @return the persisted entity.
     */
    SourceDTO save(SourceDTO sourceDTO);

    /**
     * Partially updates a source.
     *
     * @param sourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SourceDTO> partialUpdate(SourceDTO sourceDTO);

    /**
     * Get all the sources.
     *
     * @return the list of entities.
     */
    List<SourceDTO> findAll();

    /**
     * Get the "id" source.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SourceDTO> findOne(Long id);

    /**
     * Delete the "id" source.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
