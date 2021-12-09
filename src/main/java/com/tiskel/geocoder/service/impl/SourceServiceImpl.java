package com.tiskel.geocoder.service.impl;

import com.tiskel.geocoder.domain.Source;
import com.tiskel.geocoder.repository.SourceRepository;
import com.tiskel.geocoder.service.SourceService;
import com.tiskel.geocoder.service.dto.SourceDTO;
import com.tiskel.geocoder.service.mapper.SourceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Source}.
 */
@Service
@Transactional
public class SourceServiceImpl implements SourceService {

    private final Logger log = LoggerFactory.getLogger(SourceServiceImpl.class);

    private final SourceRepository sourceRepository;

    private final SourceMapper sourceMapper;

    public SourceServiceImpl(SourceRepository sourceRepository, SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    @Override
    public SourceDTO save(SourceDTO sourceDTO) {
        log.debug("Request to save Source : {}", sourceDTO);
        Source source = sourceMapper.toEntity(sourceDTO);
        source = sourceRepository.save(source);
        return sourceMapper.toDto(source);
    }

    @Override
    public Optional<SourceDTO> partialUpdate(SourceDTO sourceDTO) {
        log.debug("Request to partially update Source : {}", sourceDTO);

        return sourceRepository
            .findById(sourceDTO.getId())
            .map(existingSource -> {
                sourceMapper.partialUpdate(existingSource, sourceDTO);

                return existingSource;
            })
            .map(sourceRepository::save)
            .map(sourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SourceDTO> findAll() {
        log.debug("Request to get all Sources");
        return sourceRepository.findAll().stream().map(sourceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDTO> findOne(Long id) {
        log.debug("Request to get Source : {}", id);
        return sourceRepository.findById(id).map(sourceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Source : {}", id);
        sourceRepository.deleteById(id);
    }
}
