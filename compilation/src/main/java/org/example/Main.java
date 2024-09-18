package org.example;

import java.security.PublicKey;
import java.util.Scanner;

public class Main {
    public Main() {

    }
    public static void main (String args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("saisir le 1 nbre");
        double nbr1 =scanner.nextDouble();

        scanner = new Scanner(System.in);
        System.out.println("saisir le 2 nbre");
        double nbr2 =scanner.nextDouble();

        System.out.println(somme(nbr2, nbr1));

    }
    public static double somme(double x, double y) {
        return x+y;
    }
}