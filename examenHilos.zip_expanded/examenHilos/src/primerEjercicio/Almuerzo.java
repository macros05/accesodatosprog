package primerEjercicio;

import java.util.LinkedList;
import java.util.Random;

class Almuerzo {
	private final LinkedList<String> comedor = new LinkedList<>();

	private final String[] nombresPitufos = { "Papa Pitufo", "Pitufina", "Filosofo", "Pintor", "Gruñón", "Bromista",
			"Dormilon", "Timido", "Bonachón ", "Romantico" };
	private final String[] listaPlatos = { "Lentejas", "Fabada", "Puchero", "Filete con patatas" };
	private final int limiteComedor = 3;
	private int numeroPitufo = 0;
	private int pitufosComedor = 0;
	private String platoComiendose;

	public LinkedList<String> getComedor() {
		return comedor;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

	public synchronized void servir() throws InterruptedException {

		while (getComedor().size() == limiteComedor) {
			wait(); // Espera si el comedor está lleno
		}
		if (numeroPitufo <= nombresPitufos.length) {
			String pitufo = nombresPitufos[numeroPitufo];
			numeroPitufo++;
			String plato = listaPlatos[new Random().nextInt(listaPlatos.length)];
			platoComiendose = plato;
			getComedor().add(pitufo);

			System.out.println("El pitufo:" + pitufo + " se esta comiendo el plato: " + platoComiendose);
			pitufosComedor++;
			notifyAll();
		}
	}

	public synchronized void consumir() throws InterruptedException {
		while (getComedor().isEmpty()) {
			wait(); // Espera si el tablero está vacío
		}
		if (pitufosComedor != 0 || finalizado) {
			String pitufo = getComedor().removeFirst();

			System.out.println("El pitufo: " + pitufo + " se ha comido el plato " + platoComiendose);
			pitufosComedor--;
			notifyAll();
		}
	}
	
	private boolean finalizado = false;
}
	class Sirviendo extends Thread {
		private final Almuerzo almuerzo;

		public Sirviendo(Almuerzo almuerzo) {
			this.almuerzo = almuerzo;

		}

		@Override
		public void run() {
			try {

				almuerzo.servir();
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	class Comiendo extends Thread {

		private final Almuerzo almuerzo;

		public Comiendo(Almuerzo almuerzo) {
			this.almuerzo = almuerzo;
		}

		@Override
		public void run() {
			try {

				almuerzo.consumir();
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();

			}
		}

	}

	

