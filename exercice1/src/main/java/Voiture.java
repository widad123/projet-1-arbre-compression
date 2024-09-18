public class Voiture {
    String marque;

    public Voiture(String marque, String modele, int anneFabrication, String couleur, int viteseActuelle) {
        this.marque = marque;
        this.modele = modele;
        this.anneFabrication = anneFabrication;
        this.couleur = couleur;
        this.viteseActuelle = viteseActuelle;
    }

    String modele;
    int anneFabrication;
    String couleur;
    int viteseActuelle;
    public Voiture demarrer(){
        System.out.println("la voiture a demmarer");
    }
    public void accelerer(){
        this.viteseActuelle-=vitesse
    }

}
