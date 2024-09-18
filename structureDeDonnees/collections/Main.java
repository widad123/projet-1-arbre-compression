package collections;

public class Main {
    public static void main(String[] args){
        Liste liste = new Liste();
        System.out.println("est vide : "+liste.isEmpty());
        liste = liste.add(5);
        liste = liste.add(6);
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        System.out.println("Taille de Liste "+liste.getSize());
        liste = liste.delete();
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        System.out.println("Taille de Liste "+liste.getSize());
        liste = liste.addNieme(liste,3,0);
        System.out.println("Taille de Liste "+liste.getSize());
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        liste = liste.deleteNieme(liste,3,0);
        System.out.println("Taille de Liste "+liste.getSize());
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        liste = liste.addNiemeBoucle(liste,3,0);
        System.out.println("Taille de Liste "+liste.getSize());
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        liste = liste.deleteNiemeBoucle(liste,3,0);
        System.out.println("Taille de Liste "+liste.getSize());
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete);
        Liste liste2 = new Liste();
        System.out.println("est vide : "+liste2.isEmpty());
        liste2 = liste2.add(2);
        liste2 = liste2.add(1);
        System.out.println("Tete de la liste "+liste2.tete+" Queue de la liste "+ liste2.queue.tete);
        liste = liste.concate(liste2);
        System.out.println("Tete de la liste "+liste.tete+" Queue de la liste "+ liste.queue.tete+" Queue de queue de la liste "+ liste.queue.queue.tete);
    }
}
