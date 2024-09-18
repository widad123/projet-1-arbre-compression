package projetArbreCompression;

public class HuffmanNode {

    private Character racine;
    private int frequency;
    private HuffmanNode gauche;
    private HuffmanNode droite;

    HuffmanNode(Character racine, int frequency) {
        this.racine = racine;
        this.frequency = frequency;
        this.gauche = null;
        this.droite = null;
    }

    HuffmanNode(int frequency) {
        this(null, frequency);
    }

    public Character getRacine() {
        return racine;
    }

    public void setRacine(Character racine) {
        this.racine = racine;
    }

    public int getFrequency() { return frequency; }

    public void setFrequency(int frequency) { this.frequency = frequency; }

    public HuffmanNode getGauche() {
        return gauche;
    }

    public void setGauche(HuffmanNode gauche) {
        this.gauche = gauche;
    }

    public HuffmanNode getDroite() {
        return droite;
    }

    public void setDroite(HuffmanNode droite) {
        this.droite = droite;
    }

    public boolean estVide(){
        HuffmanNode noeudVide = new HuffmanNode(0);
        return this.equals(noeudVide);
    }

    public int getSize(){
        int taille = 0;
        if (this.estVide()){
            return 0;
        }
            return taille + (1 + this.gauche.getSize() + this.droite.getSize());

    }

    public int getHeight(){

        if (this.estVide()){
           return -1;
        }
        return (1 + Math.max(this.droite.getHeight(), this.gauche.getHeight()));
    }

    public String getPrefixe(){

        if (this.estVide()){
            return "";
        }
        return this.racine +" " +this.gauche.getPrefixe() + this.droite.getPrefixe();
    }


    public String getPostfixe(){
        String value = "";
        if (this.estVide()){
            return "";
        }
        return this.gauche.getPostfixe()  +this.droite.getPostfixe()+this.racine +" " ;
    }

    public String getInfixe(){
        String value = "";
        if (this.estVide()){
            return "";
        }
        return  this.gauche.getInfixe() +this.racine +" "+ this.droite.getInfixe();
    }

    public boolean estABR(){
        if(this.estVide()){
            return  true;
        }
        if (!this.gauche.estVide() && this.gauche.racine > this.racine) {
                return false;
        }else if (!this.droite.estVide() && this.droite.racine < this.racine) {
               return false;
        }
        return this.gauche.estABR() && this.droite.estABR();
    }

    public boolean estEquilibre(){
        if (this.estVide()){
            return true;
        }
        int delta = this.gauche.getHeight() - this.droite.getHeight();
         return ( delta == -1 || delta == 1 || delta == 0) && this.gauche.estEquilibre() && this.droite.estEquilibre();
    }

}
