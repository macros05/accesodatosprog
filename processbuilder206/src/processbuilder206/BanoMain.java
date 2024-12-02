package processbuilder206;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class BanoMain {
    public static void main(String[] args) {
        Semaphore semaforo = new Semaphore(3); // Hay 3 baños disponibles

        // Simulación en un bucle indefinido
        while (true) {
            String nombrePersona = generarNombrePersona();
            Bano tarea = new Bano(semaforo, nombrePersona);

            Thread hilo = new Thread(tarea);
            hilo.start();

            // Esperar un tiempo más largo entre la generación de hilos
            try {
                Thread.sleep(new Random().nextInt(4000) + 2000); // 2 a 6 segundos entre cada hilo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // Rompe el bucle si se interrumpe
            }
        }
    }

    // Método para generar nombres aleatorios de personas
    private static String generarNombrePersona() {
        String[] nombres = {"Pepito", "Jaimito", "María", "Juan", "Lucía", "Carlos", "Ana"};
        return nombres[new Random().nextInt(nombres.length)];
    }
}