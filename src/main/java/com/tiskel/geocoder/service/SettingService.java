package com.tiskel.geocoder.service;

import com.tiskel.geocoder.service.dto.SettingDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.tiskel.geocoder.domain.Setting}.
 */
public interface SettingService {
    /**
     * Save a setting.
     *
     * @param settingDTO the entity to save.
     * @return the persisted entity.
     */
    SettingDTO save(SettingDTO settingDTO);

    /**
     * Partially updates a setting.
     *
     * @param settingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettingDTO> partialUpdate(SettingDTO settingDTO);

    /**
     * Get all the settings.
     *
     * @return the list of entities.
     */
    List<SettingDTO> findAll();

    /**
     * Get the "id" setting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettingDTO> findOne(Long id);

    /**
     * Delete the "id" setting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
