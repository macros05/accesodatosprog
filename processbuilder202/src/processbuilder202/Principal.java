package processbuilder202;

public class Principal {

    public static void main(String[] args) {
        Raton tinky = new Raton("Tinky", 10);
        Raton winky = new Raton("Winky", 8);
        Raton poo = new Raton("Pooo", 15);
        Raton lala = new Raton("Lalala", 7);

        // Crear hilos
        tinky.start();
        winky.start();
        poo.start();
        lala.start();

        // Monitorear el estado del hilo tinky
        Thread hilo = tinky;
        Thread.State estadoAnterior = null;

        while (hilo.getState() != Thread.State.TERMINATED) {
            Thread.State estadoActual = hilo.getState();
            
            // Imprimir el estado solo si ha cambiado
            if (estadoActual != estadoAnterior) {
                System.out.println("El estado del hilo " + hilo.getName() + " ha cambiado a: " + estadoActual);
                estadoAnterior = estadoActual;
            }

            // Pequeña pausa para evitar imprimir en exceso
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Imprimir el estado final
        System.out.println("El estado final del hilo " + hilo.getName() + " es: " + hilo.getState());

        // Esperar a que terminen todos los hilos
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
}
