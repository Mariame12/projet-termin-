package com.projet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.projet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FichePresenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FichePresence.class);
        FichePresence fichePresence1 = new FichePresence();
        fichePresence1.setId(1L);
        FichePresence fichePresence2 = new FichePresence();
        fichePresence2.setId(fichePresence1.getId());
        assertThat(fichePresence1).isEqualTo(fichePresence2);
        fichePresence2.setId(2L);
        assertThat(fichePresence1).isNotEqualTo(fichePresence2);
        fichePresence1.setId(null);
        assertThat(fichePresence1).isNotEqualTo(fichePresence2);
    }
}
