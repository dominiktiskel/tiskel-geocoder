package com.tiskel.geocoder.service.impl;

import com.tiskel.geocoder.domain.PoiGroup;
import com.tiskel.geocoder.repository.PoiGroupRepository;
import com.tiskel.geocoder.service.PoiGroupService;
import com.tiskel.geocoder.service.dto.PoiGroupDTO;
import com.tiskel.geocoder.service.mapper.PoiGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PoiGroup}.
 */
@Service
@Transactional
public class PoiGroupServiceImpl implements PoiGroupService {

    private final Logger log = LoggerFactory.getLogger(PoiGroupServiceImpl.class);

    private final PoiGroupRepository poiGroupRepository;

    private final PoiGroupMapper poiGroupMapper;

    public PoiGroupServiceImpl(PoiGroupRepository poiGroupRepository, PoiGroupMapper poiGroupMapper) {
        this.poiGroupRepository = poiGroupRepository;
        this.poiGroupMapper = poiGroupMapper;
    }

    @Override
    public PoiGroupDTO save(PoiGroupDTO poiGroupDTO) {
        log.debug("Request to save PoiGroup : {}", poiGroupDTO);
        PoiGroup poiGroup = poiGroupMapper.toEntity(poiGroupDTO);
        poiGroup = poiGroupRepository.save(poiGroup);
        return poiGroupMapper.toDto(poiGroup);
    }

    @Override
    public Optional<PoiGroupDTO> partialUpdate(PoiGroupDTO poiGroupDTO) {
        log.debug("Request to partially update PoiGroup : {}", poiGroupDTO);

        return poiGroupRepository
            .findById(poiGroupDTO.getId())
            .map(existingPoiGroup -> {
                poiGroupMapper.partialUpdate(existingPoiGroup, poiGroupDTO);

                return existingPoiGroup;
            })
            .map(poiGroupRepository::save)
            .map(poiGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiGroupDTO> findAll() {
        log.debug("Request to get all PoiGroups");
        return poiGroupRepository.findAll().stream().map(poiGroupMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PoiGroupDTO> findOne(Long id) {
        log.debug("Request to get PoiGroup : {}", id);
        return poiGroupRepository.findById(id).map(poiGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PoiGroup : {}", id);
        poiGroupRepository.deleteById(id);
    }
}
