package com.chat.client;

import com.commun.evenement.Evenement;
import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;

/**
 * Cette classe représente un gestionnaire d'événement d'un client. Lorsqu'un client reçoit un texte d'un serveur,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementClient implements GestionnaireEvenement {
    private Client client;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient(Client client) {
        this.client = client;
    }
    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un serveur.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        String typeEvenement, arg;
        String[] membres, invAlias;

        if (source instanceof Connexion) {
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "END" : //Le serveur demande de fermer la connexion
                    client.deconnecter(); //On ferme la connexion
                    break;
                case "LIST" : //Le serveur a renvoyé la liste des connectés
                    arg = evenement.getArgument();
                    membres = arg.split(":");
                    System.out.println("\t\t"+membres.length+" personnes dans le salon :");
                    for (String s:membres)
                        System.out.println("\t\t\t- "+s);
                    break;
                /******************* CHAT PUBLIC *******************/
                case "HIST" : //Le serveur a renvoyé l'historique des messages du chat public
                    arg = evenement.getArgument();
                    membres = arg.split("\n");
                    for (String s:membres)
                        System.out.println("\t\t\t."+s);
                    break;
                /******************* CHAT PRIVÉ *******************/
                case "JOIN" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " vous a envoyé une invitation à un chat privé (JOIN/DECLINE alias " +
                            "pour accepter ou refuser)");
                    break;
                case "JOINOK" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " Vous êtes en chat privé avec "+arg+" (PRV alias msg pour lui envoyer " +
                            "un message en privé)");
                    break;
                case "DECLINE" :
                    arg = evenement.getArgument();
                    System.out.println(arg + " a refuse/annule l'invitation a chatter en prive.");
                    break;
                case "INV" : //Le serveur a renvoyé la liste des invitations reçues
                    arg = evenement.getArgument();
                    invAlias = arg.split(":");
                    System.out.println("\t\tInvitations reçues :");
                    for (String s:invAlias)
                        System.out.println("\t\t\t- "+s);
                    break;
                case "QUIT" :
                    arg = evenement.getArgument();
                    System.out.println(arg +" a quitté le salon privé.");
                    break;
                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Afficher le texte recu du serveur :
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
}