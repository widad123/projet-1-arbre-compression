public class Point {
    int abscisse;
    int ordonnee;

    public Point(int abscisse,int ordonnee){
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
    }
    public void afficher(){
        System.out.println("abscisse = "+ abscisse);
        System.out.println("ordonnee = "+ ordonnee);

    }
}
