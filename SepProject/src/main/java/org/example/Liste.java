package org.example;

public class Liste {
    Integer tete;
    Liste queue;
    public Liste(){
        this.tete=tete;
        this.queue=queue;

    }

    public static Liste createListe() {
        return new Liste();
    }

    public boolean estVide (){
        if (this.estVide()){
        return true;
        } else {
                return false;
        }
    }

    public Liste(Integer tete, Liste queue) {
        this.tete = tete;
        this.queue = queue;
    }

    public Liste add(Integer tete) {
        if (this.queue == null) {
            return new Liste(tete, null);
        } else {
            this.queue.add(tete);
        }
        return this;
    }
    public int size() {
        if (this.tete == null) {
            return 0;
        } else {
            return 1 + (this.queue.size() == 0 ? 0 : this.queue.size());
        }
    }


        public Liste delete(){
            if (this.estVide()){
                return null;

            }

            else {
            return new Liste(this.queue.tete, this.queue.queue);
            }


            }
    public Liste addnime(int position,int e){
        if (this.estVide()){
            System.out.println("impossible la liste est vide");
        }
        else {
            return new Liste(this.queue.tete, this.queue.addnime(position-1,e));
        }
    }

}







