package com.tiskel.geocoder.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoiGroupMapperTest {

    private PoiGroupMapper poiGroupMapper;

    @BeforeEach
    public void setUp() {
        poiGroupMapper = new PoiGroupMapperImpl();
    }
}
