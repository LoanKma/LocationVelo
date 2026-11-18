package fr.locationvelo.notification;

/**
 * Envoi de notifications. Aucune implémentation réelle (e-mail, SMS, push) n'est demandée :
 * cette dépendance est remplacée par un mock Mockito dans les tests.
 */
public interface ServiceNotification {

    void envoyer(String destinataire, String message);
}
