package com.formation.veloloc.service;

import com.formation.veloloc.depot.DepotClient;
import com.formation.veloloc.depot.DepotVelocipede;
import com.formation.veloloc.exception.ClientInconnuException;
import com.formation.veloloc.exception.QuotaLocationAtteintException;
import com.formation.veloloc.exception.VelocipedeIndisponibleException;
import com.formation.veloloc.modele.Client;
import com.formation.veloloc.modele.Velocipede;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionnaireLocationTest {

    @Mock
    private DepotVelocipede depotVelocipede;

    @Mock
    private DepotClient depotClient;

    @Mock
    private NotificateurLocation notificateurLocation;

    private final RegleLocation regleLocation = new RegleLocation();

    private GestionnaireLocation gestionnaire;

    @BeforeEach
    void initialiser() {
        gestionnaire = new GestionnaireLocation(
                depotVelocipede,
                depotClient,
                regleLocation,
                notificateurLocation);
    }

    @Test
    void uneLocationNominaleMetAJourLesEtatsEtNotifie() {
        Velocipede velo = new Velocipede("V001", "Ville", 12.0, true);
        Client client = new Client("C001", "Ada");

        when(depotVelocipede.rechercherParIdentifiant("V001"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("C001"))
                .thenReturn(Optional.of(client));

        LocalDate avant = LocalDate.now();
        gestionnaire.louer("V001", "C001");
        LocalDate apres = LocalDate.now();

        // États
        assertFalse(velo.estDisponible());
        assertEquals(1, client.getLocationsEnCours());

        // Notification avec une date de retour à J+7
        ArgumentCaptor<LocalDate> dateRetour = ArgumentCaptor.forClass(LocalDate.class);
        verify(notificateurLocation).notifierConfirmationLocation(eq(client), eq(velo), dateRetour.capture());
        // avant/après : le test reste juste même si l'appel chevauche minuit
        assertTrue(dateRetour.getValue().equals(avant.plusDays(7))
                || dateRetour.getValue().equals(apres.plusDays(7)));
    }

    @Test
    void unVeloInconnuLeveUneExceptionEtNeNotifiePas() {
        when(depotVelocipede.rechercherParIdentifiant("inconnu"))
                .thenReturn(Optional.empty());

        assertThrows(VelocipedeIndisponibleException.class,
                () -> gestionnaire.louer("inconnu", "C001"));

        verify(notificateurLocation, never()).notifierConfirmationLocation(any(), any(), any());
        verifyNoInteractions(depotClient);
    }

    @Test
    void unClientInconnuLeveUneExceptionEtNeNotifiePas() {
        Velocipede velo = new Velocipede("V002", "VTT", 18.0, true);

        when(depotVelocipede.rechercherParIdentifiant("V002"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("inconnu"))
                .thenReturn(Optional.empty());

        assertThrows(ClientInconnuException.class,
                () -> gestionnaire.louer("V002", "inconnu"));

        assertTrue(velo.estDisponible());
        verify(notificateurLocation, never()).notifierConfirmationLocation(any(), any(), any());
    }

    @Test
    void unVeloIndisponibleLeveUneExceptionEtNeNotifiePas() {
        Velocipede velo = new Velocipede("V003", "Cargo", 20.0, false);
        Client client = new Client("C003", "Grace");

        when(depotVelocipede.rechercherParIdentifiant("V003"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("C003"))
                .thenReturn(Optional.of(client));

        assertThrows(VelocipedeIndisponibleException.class,
                () -> gestionnaire.louer("V003", "C003"));

        assertEquals(0, client.getLocationsEnCours());
        verify(notificateurLocation, never()).notifierConfirmationLocation(any(), any(), any());
    }

    @Test
    void unQuotaAtteintLeveUneExceptionEtNeNotifiePas() {
        Velocipede velo = new Velocipede("V004", "Ville", 12.0, true);
        Client client = new Client("C004", "Rosalind");
        for (int i = 0; i < RegleLocation.QUOTA_MAXIMUM; i++) {
            client.incrementerLocations();
        }

        when(depotVelocipede.rechercherParIdentifiant("V004"))
                .thenReturn(Optional.of(velo));
        when(depotClient.rechercherParIdentifiant("C004"))
                .thenReturn(Optional.of(client));

        assertThrows(QuotaLocationAtteintException.class,
                () -> gestionnaire.louer("V004", "C004"));

        assertTrue(velo.estDisponible());
        assertEquals(RegleLocation.QUOTA_MAXIMUM, client.getLocationsEnCours());
        verify(notificateurLocation, never()).notifierConfirmationLocation(any(), any(), any());
    }
}
