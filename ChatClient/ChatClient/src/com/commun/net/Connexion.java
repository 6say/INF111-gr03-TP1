package com.commun.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Cette classe represente un point de connexion d'un client vers un serveur ou d'un serveur vers un client.
 * Encapsule le socket utilise pour la connexion ainsi que les flux de caracteres pour envoyer et recevoir du texte.
 *
 * @author Abdelmoumene Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Connexion {

    private Socket socket;
    private PrintWriter os;
    private BufferedInputStream is;
    private String alias;

    /**
     * Construit une connexion sur un socket, initialisant les flux de caracteres utilises par le socket.
     *
     * @param s Socket Le socket sur lequel la connexion est creee
     */
    public Connexion(Socket s) {
        try {
            socket = s;
            is = new BufferedInputStream(socket.getInputStream());
            os = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
        }
    }

    /**
     * Verifie si du texte est arrive sur la connexion et le retourne. Retourne la chaine vide s'il n'y a pas de texte.
     *
     * @return String le texte reeu, ou la chaine vide, si aucun texte n'est arrive.
     */
    public String getAvailableText() {
        String t = "";
        try {
            byte buf[] = new byte[2000];    //buffer de lecture

            if (is.available() <= 0)
                return "";
            //Lire le inputstream
            is.read(buf);
            t = (new String(buf)).trim();
            //System.out.println(texte);
            //Effacer le buffer
            buf = null;
        } catch (IOException e) {
        }
        return t;
    }

    /**
     * Envoie un texte sur la connexion
     *
     * @param texte String texte envoye
     */
    public void envoyer(String texte) {
        os.print(texte);
        os.flush();
    }

    /**
     * Ferme la connexion en fermant le socket et les flux utilises.
     *
     * @return true si la connexion a ete fermee correctement et false, sinon.
     */
    public boolean close() {
        try {
            //envoyer("Connexion closed !");
            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}