package segundoEjercicio;

import java.util.LinkedList;

class Cola {
	private final LinkedList<String> cola = new LinkedList<>();
	private final String pedido = "PEDIDO1";
	private final int colaCapacidad = 3;
	private  int pedidosCreados;
	public int getPedidosCreados() {
		return pedidosCreados;
	}

	public void setPedidosCreados(int pedidosCreados) {
		this.pedidosCreados = pedidosCreados;
	}

	public int getPedidosEnCola() {
		return pedidosEnCola;
	}

	public void setPedidosEnCola(int pedidosEnCola) {
		this.pedidosEnCola = pedidosEnCola;
	}

	public String getPedido() {
		return pedido;
	}

	public int getColaCapacidad() {
		return colaCapacidad;
	}

	private int pedidosEnCola  = 0;
	public synchronized void producir() throws InterruptedException {
		if (pedidosCreados < 20) { 
			getCola().add(pedido);
			System.out.println("Se acaba de crear un pedido " + pedido);
			pedidosEnCola++;
			pedidosCreados++;
			notifyAll(); // Notifica a los consumidores
		}
	}

	public synchronized void consumir(int idPedido) throws InterruptedException {
		while (getCola().isEmpty()) {
			wait(); 
		}
		String pedidoQuitado = getCola().removeFirst();
		System.out.println("Se acaba de finalizar el pedido " +  pedidoQuitado );
		notifyAll(); // Notifica al productor
	}

	public LinkedList<String> getCola() {
		return cola;
	}
}

class Productor extends Thread {
	private final Cola cola;

	public Productor(Cola cola) {
		this.cola = cola;
	}

	@Override
	public void run() {
		try {
			while (!cola.getCola().isEmpty() || cola.getPedidosEnCola() < cola.getColaCapacidad() ) {
				cola.producir();
				Thread.sleep(2000);
			}
			System.out.println("Se ha producido un pedido");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}

class Consumidor extends Thread {
	private final Cola cola;
	private final int idPedido;
	public Consumidor(Cola cola, int idPedido) {
		this.cola = cola;
		this.idPedido = idPedido;
	}

	@Override
	public void run() {
		try {
				cola.consumir(idPedido);
				Thread.sleep(3000); 
			
			System.out.println("El pedido con " + idPedido + " se ha entregado");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
