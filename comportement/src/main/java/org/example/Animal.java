package org.example;

public class Animal {
    String categorie;
    String nom;
    String etat;

    public Animal(String categorie, String nom, int age) {
        this.categorie = categorie;
        this.nom = nom;
        this.age = age;
        this.etat ="manger";
    }

    int age;

    public void dormir(){
        this.etat ="dormir";
        System.out.println(this.nom +"est entrain de" + etat);
    }
    public void repas(String repas){
        this.etat ="manger";
        System.out.println(this.nom +"est entrain de manger" + repas);
    }
    public void jouer(String jouer){
        this.etat="jouer";
        System.out.println(this.nom+"est entrain de jouer"+jouer);
    }
    public void showInfo(){
        System.out.println("le"+this.categorie+":" +this.nom+"age"+this.age);
        System.out.println("elle est entrai de "+etat);
    }

}

