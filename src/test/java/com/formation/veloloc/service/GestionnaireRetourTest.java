package com.formation.veloloc.service;

import com.formation.veloloc.depot.DepotClient;
import com.formation.veloloc.depot.DepotVelocipede;
import com.formation.veloloc.exception.ClientInconnuException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionnaireRetourTest {

    @Mock
    private DepotVelocipede depotVelocipede;

    @Mock
    private DepotClient depotClient;

    private GestionnaireRetour gestionnaire;

    @BeforeEach
    void initialiser() {
        gestionnaire = new GestionnaireRetour(depotVelocipede, depotClient);
    }

    @Test
    void unRetourMetLeVeloDisponibleEtDiminueLeCompteurDuClient() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, false);
        Client client = new Client("C001", "Ada");
        client.incrementerLocations();

        when(depotVelocipede.rechercherParIdentifiant("V001"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("C001"))
                .thenReturn(Optional.of(client));

        gestionnaire.retourner("V001", "C001");

        assertTrue(velo.estDisponible());
        assertEquals(0, client.getLocationsEnCours());
    }

    @Test
    void unVeloInconnuLeveUneExceptionSansRechercherLeClient() {
        when(depotVelocipede.rechercherParIdentifiant("inconnu"))
                .thenReturn(Optional.empty());

        assertThrows(VelocipedeIndisponibleException.class,
                () -> gestionnaire.retourner("inconnu", "C001"));

        verifyNoInteractions(depotClient);
    }

    @Test
    void unClientInconnuLeveUneExceptionEtLeVeloResteIndisponible() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, false);

        when(depotVelocipede.rechercherParIdentifiant("V001"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("inconnu"))
                .thenReturn(Optional.empty());

        assertThrows(ClientInconnuException.class,
                () -> gestionnaire.retourner("V001", "inconnu"));

        assertFalse(velo.estDisponible());
    }
}
