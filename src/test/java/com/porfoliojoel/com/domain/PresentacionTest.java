package com.porfoliojoel.com.domain;

import static com.porfoliojoel.com.domain.PresentacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.porfoliojoel.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresentacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presentacion.class);
        Presentacion presentacion1 = getPresentacionSample1();
        Presentacion presentacion2 = new Presentacion();
        assertThat(presentacion1).isNotEqualTo(presentacion2);

        presentacion2.setId(presentacion1.getId());
        assertThat(presentacion1).isEqualTo(presentacion2);

        presentacion2 = getPresentacionSample2();
        assertThat(presentacion1).isNotEqualTo(presentacion2);
    }
}
