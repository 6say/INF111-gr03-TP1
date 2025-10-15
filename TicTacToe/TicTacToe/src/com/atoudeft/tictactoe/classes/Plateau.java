package com.atoudeft.tictactoe.classes;

import com.atoudeft.tictactoe.MethodeNonImplementeeException;

import java.util.List;
public final class Plateau {
    private final Symbole[][] grille = new Symbole[3][3];
    private int casesRemplies = 0;
    public Symbole get(int ligne, int colonne) { return grille[ligne][colonne]; }
    public boolean estVide(Position p) { return grille[p.getLigne()][p.getColonne()] == null; }
    public int getNombreCasesRemplies() { return casesRemplies; }
    public boolean estPlein() { return casesRemplies == 9; }
    public boolean placer(Coup coup) {
        throw new MethodeNonImplementeeException("***** Vous n'avez pas encore implemente la methode : "
                +Thread.currentThread().getStackTrace()[1].getMethodName()
                +"() de la classe "+this.getClass().getName());
    }
    public List<Position> ligneGagnante() {
        int[][][] lignes = {
            {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}},
            {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}},
            {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}}
        };
        throw new MethodeNonImplementeeException("***** Vous n'avez pas encore implemente la methode : "
                +Thread.currentThread().getStackTrace()[1].getMethodName()
                +"() de la classe "+this.getClass().getName());
    }
}