package com.tiskel.geocoder.service.impl;

import com.tiskel.geocoder.domain.Poi;
import com.tiskel.geocoder.repository.PoiRepository;
import com.tiskel.geocoder.service.PoiService;
import com.tiskel.geocoder.service.dto.PoiDTO;
import com.tiskel.geocoder.service.mapper.PoiMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Poi}.
 */
@Service
@Transactional
public class PoiServiceImpl implements PoiService {

    private final Logger log = LoggerFactory.getLogger(PoiServiceImpl.class);

    private final PoiRepository poiRepository;

    private final PoiMapper poiMapper;

    public PoiServiceImpl(PoiRepository poiRepository, PoiMapper poiMapper) {
        this.poiRepository = poiRepository;
        this.poiMapper = poiMapper;
    }

    @Override
    public PoiDTO save(PoiDTO poiDTO) {
        log.debug("Request to save Poi : {}", poiDTO);
        Poi poi = poiMapper.toEntity(poiDTO);
        poi = poiRepository.save(poi);
        return poiMapper.toDto(poi);
    }

    @Override
    public Optional<PoiDTO> partialUpdate(PoiDTO poiDTO) {
        log.debug("Request to partially update Poi : {}", poiDTO);

        return poiRepository
            .findById(poiDTO.getId())
            .map(existingPoi -> {
                poiMapper.partialUpdate(existingPoi, poiDTO);

                return existingPoi;
            })
            .map(poiRepository::save)
            .map(poiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findAll() {
        log.debug("Request to get all Pois");
        return poiRepository.findAll().stream().map(poiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PoiDTO> findOne(Long id) {
        log.debug("Request to get Poi : {}", id);
        return poiRepository.findById(id).map(poiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Poi : {}", id);
        poiRepository.deleteById(id);
    }
}
