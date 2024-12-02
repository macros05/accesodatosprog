package processbuilder205;

public class PizzeriaMain {
    public static void main(String[] args) {
        Pizzeria pizzeria = new Pizzeria();

        // Crear un hilo productor (Horno)
        Horno horno = new Horno(pizzeria);

        // Crear 3 hilos consumidores (Repartidores)
        Repartidor repartidor1 = new Repartidor(pizzeria, 1);
        Repartidor repartidor2 = new Repartidor(pizzeria, 2);
        Repartidor repartidor3 = new Repartidor(pizzeria, 3);

        // Iniciar los hilos
        horno.start();
        repartidor1.start();
        repartidor2.start();
        repartidor3.start();

        // Esperar a que todos los hilos terminen
        try {
            horno.join();
            repartidor1.join();
            repartidor2.join();
            repartidor3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("La pizzería ha cerrado.");
    }
}