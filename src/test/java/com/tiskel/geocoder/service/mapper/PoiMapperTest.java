package com.tiskel.geocoder.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoiMapperTest {

    private PoiMapper poiMapper;

    @BeforeEach
    public void setUp() {
        poiMapper = new PoiMapperImpl();
    }
}
