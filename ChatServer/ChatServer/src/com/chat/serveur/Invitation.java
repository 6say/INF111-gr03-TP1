package com.chat.serveur;

import java.net.ConnectException;
import com.commun.net.Connexion;
import java.sql.Connection;

public class Invitation extends Serveur{

    private String aliasHote, aliasInvite;

    /**
     * Cree un serveur qui va ecouter sur le port specifie.
     *
     * @param port int Port d'ecoute du serveur
     */
    public Invitation(int port) {
        super(port);
    }


    public boolean equals(String alias1,String alias2) {
        boolean b = false;
        for (Connexion cnx:connectes){
            if(alias1.equals(alias2))
               b= true;
        }
    return b;
    }
}
