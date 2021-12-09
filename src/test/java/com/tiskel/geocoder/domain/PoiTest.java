package com.tiskel.geocoder.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.geocoder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poi.class);
        Poi poi1 = new Poi();
        poi1.setId(1L);
        Poi poi2 = new Poi();
        poi2.setId(poi1.getId());
        assertThat(poi1).isEqualTo(poi2);
        poi2.setId(2L);
        assertThat(poi1).isNotEqualTo(poi2);
        poi1.setId(null);
        assertThat(poi1).isNotEqualTo(poi2);
    }
}
