package com.chat.serveur;

import com.commun.evenement.Evenement;
import com.commun.evenement.EvenementUtil;
import com.commun.evenement.GestionnaireEvenement;
import com.commun.net.Connexion;
import com.commun.thread.Lecteur;
import com.commun.thread.ThreadEcouteurDeTexte;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ListIterator;
import java.util.Vector;

/**
 * Cette classe represente un serveur sur lequel des clients peuvent se connecter.
 *
 * @author Abdelmoumene Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Serveur implements Lecteur {

    //Liste des connectes au serveur :
    protected final Vector<Connexion> connectes = new Vector<>();

    //Nouveaux clients qui ne se sont pas encore "identifies":
    private final Vector<Connexion> nouveaux = new Vector<>();
    //Ce thread s'occupe d'interagir avec les nouveaux pour valider leur connexion :
    private Thread threadNouveaux;
    private int port = 8888;
    //Thred qui attend de nouvelles connexions :
    private ThreadEcouteurDeConnexions ecouteurConnexions;
    //Thread qui ecoute l'arrivee de texte des clients connectes :
    private ThreadEcouteurDeTexte ecouteurTexte;
    //Le serveur-socket utilise par le serveur pour attendre que les clients se connectent :
    private ServerSocket serverSocket;
    //Indique si le serveur est deje demarre ou non :
    private boolean demarre;
    //ecouteur qui gere les evenements correspondant e l'arrivee de texte de clients :
    protected GestionnaireEvenement gestionnaireEvenementServeur;

    /**
     * Cree un serveur qui va ecouter sur le port specifie.
     *
     * @param port int Port d'ecoute du serveur
     */
    public Serveur(int port) {
        this.port = port;
    }

    /**
     * Demarre le serveur, s'il n'a pas deje ete demarre. Demarre le thread qui ecoute l'arrivee de clients et le
     * qui ecoute l'arrivee de texte. Mets en place le gestionnaire des evenements du serveur.
     *
     * @return boolean true, si le serveur a ete demarre correctement, false, si le serveur a deje ete demarre ou si
     */
    public boolean demarrer() {
        if (demarre) //Serveur deja demarre.
            return false;
        try {
            serverSocket = new ServerSocket(port);
            ecouteurConnexions = new ThreadEcouteurDeConnexions(this);
            ecouteurConnexions.start();
            ecouteurTexte = new ThreadEcouteurDeTexte(this);
            ecouteurTexte.start();
            gestionnaireEvenementServeur = new GestionnaireEvenementServeur(this);
            demarre = true;
            return true;
        } catch (IOException e) {
            System.out.println("serveurSocket erreur : " + e.getMessage());
        }
        return false;
    }

    /**
     * Arrete le serveur en arretant les threads qui ecoutent l'arrivee de client, l'arrivee de texte et le traitement
     * des nouveaux clients.
     */
    public void arreter() {
        ListIterator<Connexion> iterateur;
        Connexion cnx;

        if (!demarre)
            return;
        ecouteurConnexions.interrupt();
        ecouteurTexte.interrupt();
        if (threadNouveaux!=null) threadNouveaux.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("serveurSocket erreur : " + e.getMessage());
        }
        //On ferme toutes les connexions apres avoir envoer "END." e chacun des clients :
        iterateur = connectes.listIterator();
        while (iterateur.hasNext()) {
            cnx = iterateur.next();
            cnx.envoyer("END.");
            cnx.close();
        }
        demarre = false;
    }

    /**
     * Cette methode bloque sur le ServerSocket du serveur jusqu'e ce qu'un client s'y connecte. Dans ce cas, elle
     * cree la connexion vers ce client et l'ajoute e la liste des nouveaux connectes.
     */
    public void attendConnexion() {
        try {
            Socket sock = serverSocket.accept();
            Connexion cnx = new Connexion(sock);
            nouveaux.add(cnx);
            System.out.println("Nouveau connecte");
            cnx.envoyer("WAIT_FOR alias");
            if (threadNouveaux == null) {
                threadNouveaux = new Thread() {
                    @Override
                    public void run() {
                        int i;
                        Connexion connexion;
                        ListIterator<Connexion> it;
                        boolean verifOK = true;
                        String hist;

                        while (!interrupted()) {
                            it = Serveur.this.nouveaux.listIterator();
                            while (it.hasNext()) {
                                connexion = it.next();

                                //Verifier ici si le client s'est bien identifie, si necessaire
                                verifOK = validerConnexion(connexion);
                                if (verifOK) {
                                    it.remove();
                                    Serveur.this.ajouter(connexion);
                                }
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                };
                threadNouveaux.start();
            }
        } catch (IOException e) {

        }
    }

    /**
     * Valide l'arrivee d'un nouveau client sur le serveur. Cette implementation
     * par defaut valide automatiquement le client en retournant true.
     * Cette methode sera redefinie dans les classes filles, comme ServerChat,
     * pour implementer une validation en fonction des besoins de l'application.
     * Par exemple, ServerChat va verifier si le nouveau client a fourni un
     * alias valide.
     *
     * @param connexion Connexion la connexion representant le client.
     * @return boolean true.
     */
    protected boolean validerConnexion(Connexion connexion) {
        return true;
    }
    /**
     * Ajoute la connexion d'un nouveau client e la liste des connectes.
     * @param connexion Connexion la connexion representant le client
     * @return boolean true, si l'ajout a ete effectue avec succes, false, sinon
     */
    public synchronized boolean ajouter(Connexion connexion) {
        System.out.println(connexion.getAlias()+" est arrive!");
        boolean res = this.connectes.add(connexion);
        return res;
    }

    public synchronized boolean enlever(Connexion connexion) {
        System.out.println(connexion.getAlias()+" est parti!");
        boolean res = this.connectes.remove(connexion);
        return res;
    }
    /**
     * Cette methode scanne tous les clients actuellement connectes e ce serveur pour verifie s'il y a du texte qui
     * arrive. Pour chaque texte qui arrive, elle cree un evenement contenant les donnees du texte et demande au
     * gestionnaire d'evenement serveur de traiter l'evenement.
     */
    public synchronized void lire() {
        ListIterator<Connexion> iterateur = connectes.listIterator();
        Connexion cnx;
        String[] t;
        Evenement evenement;
        for (int i=0;i<connectes.size();i++) {
            cnx = connectes.get(i);
            String texte = cnx.getAvailableText();
            if (!"".equals(texte)) {
                t = EvenementUtil.extraireInfosEvenement(texte);
                evenement = new Evenement(cnx, t[0], t[1]);
                gestionnaireEvenementServeur.traiter(evenement);
            }
        }
    }

    /**
     * Retourne le port d'ecoute de ce serveur
     *
     * @return int Le port d'ecoute
     */
    public int getPort() {
        return port;
    }

    /**
     * Specifie le port d'ecoute du serveur.
     *
     * @param port int Le port d'ecoute
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * Indique si le serveur a ete demarre.
     *
     * @return boolean true si le serveur est demarre et false sinon
     */
    public boolean isDemarre() {
        return demarre;
    }
}