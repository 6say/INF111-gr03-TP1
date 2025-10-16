package com.chat.serveur;

public class Invitation {

    private String aliasHote, aliasInvite;


    public boolean equals(String alias1,String alias2) {
        if(alias1.equals(alias2))
            return true;

        return false;
    }
}
