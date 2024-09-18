package arbres;

public class AB {

    private Integer racine;
    private  AB gauche;
    private  AB droite;

    static final AB noeudVide = new AB();

    public AB(Integer racine,AB abg, AB abd) {
        this.racine = racine;
        this.droite = abd;
        this.gauche = abg;
    }

    public AB(Integer racine) {
        this.racine = racine;
        this.droite = noeudVide;
        this.gauche = noeudVide;
    }

    public AB() {
        this.racine = null;
        this.droite = null;
        this.gauche = null;
    }

    public Integer getRacine() {
        return racine;
    }

    public void setRacine(Integer racine) {
        this.racine = racine;
    }

    public AB getGauche() {
        return gauche;
    }

    public void setGauche(AB gauche) {
        this.gauche = gauche;
    }

    public AB getDroite() {
        return droite;
    }

    public void setDroite(AB droite) {
        this.droite = droite;
    }

    public boolean estVide(){
        return this == noeudVide;
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
