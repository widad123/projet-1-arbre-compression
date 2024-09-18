package arbres;


public class Main {
    public static void main(String[] args){
        AB abg1 = new AB(1);
        AB abd1 = new AB(4);
        AB abg = new AB(2,abg1,abd1);
        AB abg2 = new AB(10);
        AB abd2 = new AB(2);
        AB abd = new AB(8,abg2,abd2);
        AB abd3 = new AB(8);
        AB ab = new AB(6,abg,abd3);

        System.out.println(ab.estVide());
        System.out.println(ab.getSize());
        System.out.println(ab.getHeight());
        System.out.println(ab.getPrefixe());
        System.out.println(ab.getPostfixe());
        System.out.println(ab.getInfixe());
        System.out.println(abd.estABR());
        System.out.println(ab.estABR());
        System.out.println(abd.estEquilibre());
        System.out.println(ab.estEquilibre());

    }
}
