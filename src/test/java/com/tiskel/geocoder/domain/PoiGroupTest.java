package com.tiskel.geocoder.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.geocoder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoiGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoiGroup.class);
        PoiGroup poiGroup1 = new PoiGroup();
        poiGroup1.setId(1L);
        PoiGroup poiGroup2 = new PoiGroup();
        poiGroup2.setId(poiGroup1.getId());
        assertThat(poiGroup1).isEqualTo(poiGroup2);
        poiGroup2.setId(2L);
        assertThat(poiGroup1).isNotEqualTo(poiGroup2);
        poiGroup1.setId(null);
        assertThat(poiGroup1).isNotEqualTo(poiGroup2);
    }
}
