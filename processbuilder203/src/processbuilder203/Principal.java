package processbuilder203;

public class Principal {
	// Variable compartida entre los hilos
	private static int contador = 0;

	public static void main(String[] args) {
		// Crear 3 hilos que incrementarán el contador en 1, 3 y 5 unidades
		// respectivamente
		Thread hilo1 = new Thread(new Incrementador(1, "Hilo 1"));
		Thread hilo3 = new Thread(new Incrementador(3, "Hilo 3"));
		Thread hilo5 = new Thread(new Incrementador(5, "Hilo 5"));

		// Lanzar los hilos 10 veces
		for (int i = 0; i < 1000; i++) {
			hilo1 = new Thread(new Incrementador(1, "Hilo 1"));
			hilo3 = new Thread(new Incrementador(3, "Hilo 3"));
			hilo5 = new Thread(new Incrementador(5, "Hilo 5"));

			hilo1.start();
			hilo3.start();
			hilo5.start();

			try {
				hilo1.join();
				hilo3.join();
				hilo5.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Resultado final esperado
		int esperado = (1 + 3 + 5) * 1000;
		System.out.println("Valor final del contador: " + getContador());
		System.out.println("Valor esperado del contador: " + esperado);
	}

	public static int getContador() {
		return contador;
	}

	public static void setContador(int contador) {
		Principal.contador = contador;
	}
}

// Clase que implementa el incremento del contador
class Incrementador implements Runnable {
	private int incremento;
	private String nombreHilo;

	public Incrementador(int incremento, String nombreHilo) {
		this.incremento = incremento;
		this.nombreHilo = nombreHilo;
	}

	@Override
	public void run() {
		// Incrementar la variable compartida
		System.out.println(nombreHilo + " suma " + incremento);
		Principal.setContador(Principal.getContador() + incremento);
	}
}
