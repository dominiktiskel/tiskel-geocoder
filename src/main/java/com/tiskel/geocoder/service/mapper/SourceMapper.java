package com.tiskel.geocoder.service.mapper;

import com.tiskel.geocoder.domain.Source;
import com.tiskel.geocoder.service.dto.SourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Source} and its DTO {@link SourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SourceMapper extends EntityMapper<SourceDTO, Source> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceDTO toDtoId(Source source);
}
