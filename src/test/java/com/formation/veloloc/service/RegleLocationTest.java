package com.formation.veloloc.service;

import com.formation.veloloc.exception.QuotaLocationAtteintException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegleLocationTest {

    private final RegleLocation regle = new RegleLocation();

    @Test
    void uneLocationNominaleEstAutorisee() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);
        Client client = new Client("C001", "Ada");

        assertDoesNotThrow(() -> regle.verifierLocationAutorisee(velo, client));
    }

    @Test
    void unVeloIndisponibleEmpecheLaLocation() {
        Velocipede velo = new Velocipede("V002", "VTT", 18.0, false);
        Client client = new Client("C002", "Alan");

        assertThrows(VelocipedeIndisponibleException.class,
                () -> regle.verifierLocationAutorisee(velo, client));
    }

    @Test
    void unClientAuQuotaNePeutPlusLouer() {
        Velocipede velo = new Velocipede("V003", "Cargo", 20.0, true);
        Client client = new Client("C003", "Grace");

        for (int i = 0; i < RegleLocation.QUOTA_MAXIMUM; i++) {
            client.incrementerLocations();
        }

        QuotaLocationAtteintException exception = assertThrows(QuotaLocationAtteintException.class,
                () -> regle.verifierLocationAutorisee(velo, client));
        assertTrue(exception.getMessage().contains("C003"));
    }

    @Test
    void unClientJusteSousLeQuotaPeutEncoreLouer() {
        Velocipede velo = new Velocipede("V004", "Ville", 12.0, true);
        Client client = new Client("C004", "Rosalind");

        for (int i = 0; i < RegleLocation.QUOTA_MAXIMUM - 1; i++) {
            client.incrementerLocations();
        }

        assertDoesNotThrow(() -> regle.verifierLocationAutorisee(velo, client));
    }

    @Test
    void leQuotaMaximumEstDeDeuxLocations() {
        assertEquals(2, RegleLocation.QUOTA_MAXIMUM);
    }

    @Test
    void unVeloNullEstRefuse() {
        Client client = new Client("C001", "Ada");

        assertThrows(IllegalArgumentException.class,
                () -> regle.verifierLocationAutorisee(null, client));
    }

    @Test
    void unClientNullEstRefuse() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);

        assertThrows(IllegalArgumentException.class,
                () -> regle.verifierLocationAutorisee(velo, null));
    }
}
