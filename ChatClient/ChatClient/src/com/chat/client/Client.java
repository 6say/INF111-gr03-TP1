/*
 Etats du client :
DISCONNECTED : le client est deconnecte
SEARCHING : le client recherche le serveur
NOTFOUND : le client n'a pas trouve le serveur
CONNECTING : le serveur a ete trouve mais le client attend que le serveur valide la demande (utilisateur+mot de passe)
REFUSED : le serveur a refuse la connexion car l'utilisateur ou son mot de passe sont incorrects
CONNECTED : le client est connecte 
DISCONNECTING : le client est entrain de se deconnecter
*/

package com.chat.client;

import java.net.Socket;
import java.io.*;

import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;
import com.commun.thread.Lecteur;
import com.commun.evenement.Evenement;
import com.commun.evenement.EvenementUtil;
import com.commun.thread.ThreadEcouteurDeTexte;

/**
 * Cette classe represente un client capable de se connecter e un serveur.
 *
 * @author Abdelmoumene Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Client implements Lecteur {

    private String adrServeur = Config.ADRESSE_SERVEUR;
    private int portServeur = Config.PORT_SERVEUR;
    private boolean connecte;
    private Connexion connexion;
    private GestionnaireEvenement gestionnaireEvenementClient;
    private ThreadEcouteurDeTexte vt;

    /**
     * Connecte le client au serveur en utilisant un socket. Si la connexion reussit, un objet
     * Connexion est cree qui cree les flux d'entree/sortie permettant de communiquer du texte
     * avec le serveur.
     *
     * @return boolean true, si la connexion a reussi. false, si la connexion echoue
     * ou si le client etait deje connecte.
     */
    public boolean connecter() {
        boolean resultat = false;
        if (this.isConnecte()) //deja connecte
            return resultat;

        try {
            Socket socket = new Socket(adrServeur, portServeur);
            connexion = new Connexion(socket);
            this.setAdrServeur(adrServeur);
            this.setPortServeur(portServeur);

            //On cree l'ecouteur d'evenements pour le client :
            gestionnaireEvenementClient = new GestionnaireEvenementClient(this);

            //Demarrer le thread inspecteur de texte:
            vt = new ThreadEcouteurDeTexte(this);
            vt.start();  //la methode run() de l'ecouteur de texte s'execute en parallele avec le reste du programme.
            resultat = true;
            this.setConnecte(true);
        } catch (IOException e) {
            this.deconnecter();
        }
        return resultat;
    }

    /**
     * Deconnecte le client, s'il est connecte, en fermant l'objet Connexion. Le texte "exit" est envoye au serveur
     * pour l'informer de la deconnexion. Le thread ecouteur de texte est arrete.
     *
     * @return boolean true, si le client s'est deconnecte, false, s'il etait deje deconnecte
     */
    public boolean deconnecter() {
        if (!isConnecte())
            return false;

        connexion.envoyer("exit");
        connexion.close();
        if (vt != null)
            vt.interrupt();
        this.setConnecte(false);
        return true;
    }
    /**
     * Cette methode verifie s'il y a du texte qui arrive sur la connexion du client et, si c'est le cas, elle cree
     * un evenement contenant les donnees du texte et demande au gestionnaire d'evenement client de traiter l'evenement.
     *
     * @author Abdelmoumene Toudeft
     * @version 1.0
     * @since   2023-09-20
     */
    public void lire() {

        String[] t;
        Evenement evenement;
        String texte = connexion.getAvailableText();

        if (!"".equals(texte)){
            t = EvenementUtil.extraireInfosEvenement(texte);
            evenement = new Evenement(connexion,t[0],t[1]);
            gestionnaireEvenementClient.traiter(evenement);
        }
    }

    /**
     * Specifie un gestionnaire d'evenements pour le client.
     * @param gestionnaireEvenementClient
     * @author Abdelmoumene Toudeft
     * @version 1.0
     * @since   2025-10-02
     */
    public void setGestionnaireEvenementClient(GestionnaireEvenement gestionnaireEvenementClient) {
        this.gestionnaireEvenementClient = gestionnaireEvenementClient;
    }

    /**
     * Cette methode retourne l'adresse IP du serveur sur lequel ce client se connecte.
     *
     * @return String l'adresse IP du serveur dans le format "192.168.25.32"
     * @author Abdelmoumene Toudeft
     * @version 1.0
     * @since   2023-09-20
     */
    public String getAdrServeur() {
        return adrServeur;
    }
    public void setAdrServeur(String adrServeur) {
        this.adrServeur = adrServeur;
    }
    /**
     * Indique si le client est connecte e un serveur..
     *
     * @return boolean true si le client est connecte et false sinon
     */
    public boolean isConnecte() {
        return connecte;
    }

    /**
     * Marque ce client comme etant connecte ou deconnecte.
     *
     * @param connecte boolean Si true, marque le client comme etant connecte, si false, le marque comme deconnecte
     */
    public void setConnecte(boolean connecte) {
        this.connecte = connecte;
    }

    /**
     * Retourne le port d'ecoute du serveur auquel ce client se connecte.
     *
     * @return int Port d'ecoute du serveur
     */
    public int getPortServeur() {
        return portServeur;
    }

    /**
     * Specifie le port d'ecoute du serveur sur lequel ce client se connecte.
     *
     * @param portServeur int Port d'ecoute du serveur
     */
    public void setPortServeur(int portServeur) {
        this.portServeur = portServeur;
    }

    /**
     * Envoie un texte au serveur en utilisant un objet Connexion.
     *
     * @param s String texte e envoyer
     */
    public void envoyer(String s) {
        this.connexion.envoyer(s);
    }
}