package processbuilder206;

import java.util.Random;
import java.util.concurrent.Semaphore;

class Bano implements Runnable {
    private final Semaphore semaforo; // Controla el acceso a los baños
    private final String nombre; // Nombre de la persona que usa el baño

    public Bano(Semaphore semaforo, String nombre) {
        this.semaforo = semaforo;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            System.out.println(nombre + " está esperando para entrar al baño.");
            Thread.sleep(new Random().nextInt(3000) + 3000); // Mayor espera antes de intentar entrar
            semaforo.acquire(); // Adquiere un permiso para entrar al baño
            System.out.println(nombre + " está usando el baño.");
            Thread.sleep(new Random().nextInt(5000) + 4000); // Simula el tiempo en el baño
            System.out.println(nombre + " salió del baño.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release(); // Libera el permiso para que otro hilo pueda entrar
        }
    }
}


