package org.example;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("saisir le nom de l'animal");
        String nomAnimal = scanner.nextLine();

        System.out.println("saisir le categorie de l'aniaml");
        String categorieAnimal = scanner.nextLine();

        System.out.println("saisir l'age de l'animal");
        int ageAnimal = scanner.nextInt();

        Animal animal =new Animal(nomAnimal,categorieAnimal,ageAnimal);
        animal.showInfo();
        System.out.println(",,,,,,,,,,,,,,,,,,,,,");
        animal.jouer("balon");
        animal.repas("pain");
        animal.dormir();



    }
}