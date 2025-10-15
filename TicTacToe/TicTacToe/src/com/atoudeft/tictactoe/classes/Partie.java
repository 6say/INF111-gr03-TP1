package com.atoudeft.tictactoe.classes;

import com.atoudeft.tictactoe.MethodeNonImplementeeException;

public final class Partie {
    private final Plateau plateau = new Plateau();
    private Symbole joueurCourant;
    private StatutPartie statut;

    public Plateau getPlateau()       {
        return plateau;
    }
    public Symbole getJoueurCourant() {
       return joueurCourant;
    }
    public StatutPartie getStatut()   {
        return statut;
    }

    public Partie(Symbole joueurCourant) {
        this.joueurCourant = joueurCourant;
        statut = StatutPartie.EN_COURS;
    }
    public Partie() {
        this(Symbole.X);
    }

    public boolean jouer(Symbole symbole, Position position) {
        throw new MethodeNonImplementeeException("***** Vous n'avez pas encore implemente la methode : "
                +Thread.currentThread().getStackTrace()[1].getMethodName()
                +"() de la classe "+this.getClass().getName());
    }
    public boolean isPartieEnCours() {
        if (statut != StatutPartie.EN_COURS) {
            return false;
        }
        return true;
    }
    private void mettreAJourStatutApresCoup() {
        throw new MethodeNonImplementeeException("***** Vous n'avez pas encore implemente la methode : "
                +Thread.currentThread().getStackTrace()[1].getMethodName()
                +"() de la classe "+this.getClass().getName());
    }

    @Override
    public String toString() {
        String str = "";
        str = plateau +"\n"
                +"Joueur Courant : " + joueurCourant +"\n"
                +"Etat : " + statut + "\n";
        return str;
    }
}