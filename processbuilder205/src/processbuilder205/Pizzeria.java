package processbuilder205;

import java.util.LinkedList;
import java.util.Random;

class Pizzeria {
    private final LinkedList<String> tablero = new LinkedList<>();
    private final int capacidad = 3; // Capacidad del tablero
    private final String[] nombresPizzas = {"Margarita", "Pepperoni", "Cuatro Quesos", "Hawaiana", "Vegana"};
    private int pizzasProducidas = 0; // Contador para limitar la producción total a 20 pizzas

    // Método para producir una pizza
    public synchronized void producir() throws InterruptedException {
        while (getTablero().size() == capacidad) {
            wait(); // Espera si el tablero está lleno
        }
        if (pizzasProducidas < 20) {
            String pizza = nombresPizzas[new Random().nextInt(nombresPizzas.length)];
            getTablero().add(pizza);
            pizzasProducidas++;
            System.out.println("El horno acaba de producir una pizza: " + pizza);
            notifyAll(); // Notifica a los consumidores
        }
    }

    // Método para consumir una pizza
    public synchronized void consumir(int idRepartidor) throws InterruptedException {
        while (getTablero().isEmpty()) {
            wait(); // Espera si el tablero está vacío
        }
        String pizza = getTablero().removeFirst();
        System.out.println("Repartidor " + idRepartidor + " retira una pizza: " + pizza);
        notifyAll(); // Notifica al productor
    }

    // Verifica si ya se produjeron todas las pizzas
    public boolean produccionCompletada() {
        return pizzasProducidas >= 20;
    }

	public LinkedList<String> getTablero() {
		return tablero;
	}
}

class Horno extends Thread {
    private final Pizzeria pizzeria;

    public Horno(Pizzeria pizzeria) {
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        try {
            while (!pizzeria.produccionCompletada()) {
                pizzeria.producir();
                Thread.sleep(2000); // Aumentado a 2000ms para mayor claridad
            }
            System.out.println("El horno ha terminado de producir todas las pizzas.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Repartidor extends Thread {
    private final Pizzeria pizzeria;
    private final int idRepartidor;

    public Repartidor(Pizzeria pizzeria, int idRepartidor) {
        this.pizzeria = pizzeria;
        this.idRepartidor = idRepartidor;
    }

    @Override
    public void run() {
        try {
            while (!pizzeria.produccionCompletada() || !pizzeria.getTablero().isEmpty()) {
                pizzeria.consumir(idRepartidor);
                Thread.sleep(3000); // Aumentado a 3000ms para mayor claridad
            }
            System.out.println("Repartidor " + idRepartidor + " ha terminado su turno.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
