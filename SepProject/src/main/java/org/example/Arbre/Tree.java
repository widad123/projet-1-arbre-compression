package org.example.Arbre;

public class Tree {
    Integer racine;
    private Tree gauche;
    private Tree droit;

    public Tree() {
        this.racine = null;
        this.droit = null;
        this.gauche = null;
    }

    public Tree(Integer racine, Tree gauche, Tree droit) {
        this.racine = racine;
        this.droit = droit;
        this.gauche = gauche;
    }

    private static final Tree arbreVide = new Tree();
    public boolean arbreVide(){
        return this==arbreVide;
    }

    public static boolean getArbreVide() {
        return arbreVide;
    }
    public int getTaille() {
        if (this.racine == null) {
            return 0;
        } else {
            return 1 + (gauche.getTaille()  + (droit.getTaille();
        }
    }
    public int getHauteur() {

        if (this.racine == null) {
            return 0;
        } else {
            int hauteurGauche = gauche.getHauteur() ;
            int hauteurDroit =droit.getHauteur();
            return 1 + Math.max(hauteurGauche, hauteurDroit);
        }
    }
    public void prefixe(){
        if(!this.arbreVide()){
            System.out.println(" " + this.racine + " ");
            this.gauche.prefixe();
            this.droit.prefixe();

        }

    }
    public void postfix(){
        if(!this.arbreVide()){
            this.gauche.postfix();
            this.droit.postfix();
            System.out.println(" " + this.racine + " ");
        }

    }

    public void infixe(){
        if(!this.arbreVide()){
            this.gauche.infixe();
            System.out.println(" " + this.racine + " ");
            this.droit.infixe();
        }

    }
    public boolean estAbr() {
        if (this.arbreVide()) {
            return true;
        }
        if (!this.gauche.arbreVide() && this.gauche.racine>this.racine)
        {
            return false;
        }
        if (!this.droit.arbreVide() && this.droit.racine<this.racine){
            return false;
        }
        return this.droit.estAbr() && this.gauche.estAbr();

          x    
o,;o    }
}
;,o!?UU?.U§98%%%µ