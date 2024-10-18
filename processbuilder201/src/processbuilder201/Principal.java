package processbuilder201;

public class Principal {

    public static void main(String[] args) {
        Raton tinky = new Raton("Tinky", 10);
        Raton winky = new Raton("Winky", 8);
        Raton poo = new Raton("Pooo", 15);
        Raton lala = new Raton("Lalala", 7);

        tinky.start();
        winky.start();
        poo.start();
        lala.start();

        try {
            tinky.join();
            winky.join();
            poo.join();
            lala.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Todos los ratones terminaron de comer.");
    }
    //Prueba funciona git
}
