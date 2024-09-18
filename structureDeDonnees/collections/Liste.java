package collections;

public class Liste {

     Integer tete;

     Liste queue;

     static  Liste listeVide = new Liste();

     public Liste(){
         this.tete = null;
         this.queue = listeVide;
     }

    public Liste(Integer tete,Liste liste) {
        this.tete = tete;
        this.queue = liste;
    }

    public Liste creer(){
        return listeVide;
    }

    public boolean isEmpty(){
        return this == listeVide;
    }

    public Liste add(Integer e){
        Liste liste;
        if (this.isEmpty()){
            liste = new Liste(e, listeVide);
        }else {
            liste = new Liste(e,this);
        }
        return liste;
    }

    public int getSize(){

        if (this.isEmpty()){
            return  0;
        }else {
           return 1+ this.queue.getSize();
        }

    }

    public Liste delete(){
        if (this.isEmpty()){
            return null;
        }
           return new Liste(this.queue.tete,this.queue.queue);
    }

    public Liste addNieme(Liste liste, Integer element, int n) {
        if (n == 0) {
            return new Liste(element, liste);
        }

        if (liste.isEmpty()) {
            System.out.println("La position dépasse la taille de la liste.");
        }

        liste.queue = addNieme(liste.queue, element,n - 1);
        return liste;
    }

    public Liste addNiemeBoucle(Liste liste, Integer element, int n) {

        if (n == 0) {
            return new Liste(element, liste);
        }

        Liste current = liste;
        for (int i = 0; i < n - 1; i++) {

            if (current.isEmpty()) {
                System.out.println("La position dépasse la taille de la liste.");
            }

            current = current.queue;
        }

        current.queue = new Liste(element, current.queue);
        return liste;

    }
    public Liste deleteNieme(Liste liste, Integer element, int n) {

        if (liste.isEmpty()) {
            System.out.println("La position dépasse la longueur de la liste.");
        }

        if (n == 0) {
            return liste.queue;
        }

        liste.queue = deleteNieme(liste.queue, element, n - 1);
        return liste;
    }

    public Liste deleteNiemeBoucle(Liste liste, Integer element, int n) {

        if (n < 0 || n >= liste.getSize()) {
            System.out.println("Position invalide");
        }

        if (n == 0) {
            return liste.queue;
        }

        Liste current = liste;
        Liste previous = null;
        int index = 0;

        while (current != null && index < n) {
            previous = current;
            current = current.queue;
            index++;
        }

        if (previous != null && current != null) {
            previous.queue = current.queue;
        }

        return liste;
    }

    public Liste concate( Liste liste){

        if (this.isEmpty() && !liste.isEmpty()){
            return liste;
        } else if (!this.isEmpty() && liste.isEmpty()) {
            return this;
        } else if (this.isEmpty() && liste.isEmpty()) {
            return listeVide;
        }else{
            Liste current = liste;
        while (!current.queue.isEmpty()){
            current = current.queue;
        }
        current.queue = this;
            return liste;
        }
    }
}
