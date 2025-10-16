package com.chat.serveur;

import com.commun.net.Connexion;

/**
 * Cette classe etend (herite) la classe abstraite Serveur et y ajoute le necessaire pour que le
 * serveur soit un serveur de chat.
 *
 * @author Abdelmoumene Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-15
 */
public class ServeurChat extends Serveur {

    /**
     * Cree un serveur de chat qui va ecouter sur le port specifie.
     *
     * @param port int Port d'ecoute du serveur
     */
    public ServeurChat(int port) {
        super(port);
    }

    @Override
    public synchronized boolean ajouter(Connexion connexion) {
        String hist = this.historique();
        if ("".equals(hist)) {
            connexion.envoyer("OK");
        }
        else {
            connexion.envoyer("HIST " + hist);
        }
        return super.ajouter(connexion);
    }
    /**
     * Valide l'arrivee d'un nouveau client sur le serveur. Cette redefinition
     * de la methode heritee de Serveur verifie si le nouveau client a envoye
     * un alias compose uniquement des caracteres a-z, A-Z, 0-9, - et _.
     *
     * @param connexion Connexion la connexion representant le client
     * @return boolean true, si le client a valide correctement son arrivee, false, sinon
     */
    @Override
    protected boolean validerConnexion(Connexion connexion) {

        String aliasFourni = connexion.getAvailableText().trim();
        char c;
        int taille;
        boolean res = true;
        if ("".equals(aliasFourni)) {
            return false;
        }
        taille = aliasFourni.length();
        for (int i=0;i<taille;i++) {
            c = aliasFourni.charAt(i);
            if ((c<'a' || c>'z') && (c<'A' || c>'Z') && (c<'0' || c>'9')
                    && c!='_' && c!='-') {
                res = false;
                break;
            }
        }
        if (!res)
            return false;
        for (Connexion cnx:connectes) {
            if (aliasFourni.equalsIgnoreCase(cnx.getAlias())) { //alias deje utilise
                res = false;
                break;
            }
        }
        if (!res)
            return false;
        connexion.setAlias(aliasFourni);
        return true;
    }

    /**
     * Retourne la liste des alias des connectes au serveur dans une chaene de caracteres.
     *
     * @return String chaene de caracteres contenant la liste des alias des membres connectes sous la
     * forme alias1:alias2:alias3 ...
     */
    public String list() {
        String s = "";
        for (Connexion cnx:connectes)
            s+=cnx.getAlias()+":";
        return s;
    }
    /**
     * Retourne la liste des messages de l'historique de chat dans une chaene
     * de caracteres.
     *
     * @return String chaene de caracteres contenant la liste des alias des membres connectes sous la
     * forme message1\nmessage2\nmessage3 ...
     */
    public String historique() {
        String s = "";
        return s;
    }

    public void envoyerATousSauf(String str,String aliasExpediteur){
        String s = "";
        for (Connexion cnx:connectes){
            if (cnx.getAlias().equalsIgnoreCase(aliasExpediteur)){

            }else {
                cnx.envoyer(aliasExpediteur + " >> " + str);
            }
        }
    }
}
