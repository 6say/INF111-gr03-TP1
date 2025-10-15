package com.atoudeft.tictactoe.programme;

import com.atoudeft.tictactoe.classes.Partie;
import com.atoudeft.tictactoe.classes.Position;
import com.atoudeft.tictactoe.classes.Symbole;

import java.util.Locale;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        Partie partie = new Partie();
        String saisie, t[];
        Symbole symbole;
        int i,j;
        clavier.useLocale(Locale.ENGLISH);

        System.out.println(partie);
        while (partie.isPartieEnCours()) {
            System.out.print("Entrez le coup dans le format : symbole ligne colonne comme par exemple X 0 2 : ");
            saisie = clavier.nextLine();
            t = saisie.split(" ");
            if (t.length<3) {
                System.out.println("Mauvaise saisie");
                continue;
            }
            if ("X".equalsIgnoreCase(t[0]))
                symbole = Symbole.X;
            else if ("O".equalsIgnoreCase(t[0]))
                symbole = Symbole.O;
            else {
                System.out.println("Mauvaise saisie");
                continue;
            }
            try {
                i = Integer.parseInt(t[1]);
                j = Integer.parseInt(t[2]);
                if (!partie.jouer(symbole,new Position(i,j)))
                    System.out.println("Coup invalide");
                System.out.println(partie);
            }
            catch (Exception exp) {
                System.out.println("Mauvaise saisie");
            }
        }
    }
}