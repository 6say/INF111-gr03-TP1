package com.chat.serveur;

import com.commun.evenement.Evenement;
import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;

/**
 * Cette classe represente un gestionnaire d'evenement d'un serveur. Lorsqu'un serveur reeoit un texte d'un client,
 * il cree un evenement e partir du texte reeu et alerte ce gestionnaire qui reagit en gerant l'evenement.
 *
 * @author Abdelmoumene Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'evenements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gere des evenements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Methode de gestion d'evenements. Cette methode contiendra le code qui gere les reponses obtenues d'un client.
     *
     * @param evenement L'evenement e gerer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur;
        String aliasInvite; //Utile pour la commande JOIN
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoye "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connectees :
                    cnx.envoyer("LIST " + serveur.list());
                    break;
                case "MSG":
                    aliasExpediteur = cnx.getAlias();
                    msg = evenement.getArgument();
                    serveur.envoyerATousSauf(msg, aliasExpediteur);

                //Ajoutez ici deautres case pour gerer deautres commandes.

                case  "JOIN" : // permet de faire une requête à un utilisateur pour rejoindre un serveur privé:
                    cnx.envoyer("Entrez l'identifiant de la personne à inviter");
                    aliasInvite = evenement.getArgument();
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}