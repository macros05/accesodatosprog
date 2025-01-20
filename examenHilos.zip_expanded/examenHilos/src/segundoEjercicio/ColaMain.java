package segundoEjercicio;

public class ColaMain {
    public static void main(String[] args) {

	Cola cola = new Cola();
	
	Productor productor = new Productor(cola);
	
	Consumidor consumidor1 = new Consumidor(cola, 1);
	Consumidor consumidor2 = new Consumidor (cola, 2); 
	Consumidor consumidor3 = new Consumidor (cola, 3); 

	productor.start();
	
	consumidor1.start();
	consumidor2.start();
	consumidor3.start();
	 
	try {
		productor.join();
		consumidor1.join();
		consumidor2.join();
		consumidor3.join();
	}
	catch(InterruptedException e) {
        Thread.currentThread().interrupt();
	}
	System.out.println("Todos los pedidos se han entregado");
    }
}
