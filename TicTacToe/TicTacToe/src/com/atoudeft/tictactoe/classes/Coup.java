package com.atoudeft.tictactoe.classes;

public final class Coup {
    private final Position position;
    private final Symbole symbole;
    public Coup(Position position, Symbole symbole) {
        this.position = position;
        this.symbole = symbole;
    }
    public Position getPosition() { return position; }
    public Symbole getSymbole()   { return symbole; }
    @Override public String toString() { return symbole + "@" + position; }
}