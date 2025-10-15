package com.atoudeft.tictactoe.classes;

public final class Position {
    private final int ligne;
    private final int colonne;
    public Position(int ligne, int colonne) {
        if (ligne < 0 || ligne > 2 || colonne < 0 || colonne > 2) {
            throw new IllegalArgumentException("Position hors plateau: (" + ligne + "," + colonne + ")");
        }
        this.ligne = ligne;
        this.colonne = colonne;
    }
    public int getLigne()   { return ligne; }
    public int getColonne() { return colonne; }

    @Override public String toString() { return "(" + ligne + "," + colonne + ")"; }
}