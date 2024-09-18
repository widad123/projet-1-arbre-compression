package org.example;

public class Voiture {
    // Attributs
    private String marque;
    private String modele;

    // Constructeur
    public Voiture(String marque, String modele) {
        this.marque = marque;
        this.modele = modele;
    }

    // Méthode
    public void afficherDetails() {
        System.out.println("Marque: " + marque + ", Modèle: " + modele);
    }
}
