package primerEjercicio;

public class AlmuerzoMain {
	
    public static void main(String[] args) {
    	
    Almuerzo almuerzo1 = new Almuerzo();
    	
	Sirviendo servir1 = new Sirviendo(almuerzo1);
	
	Comiendo comer1 = new Comiendo(almuerzo1);
	Comiendo comer2 = new Comiendo(almuerzo1);

	servir1.start();
	
	comer1.start();
	comer2.start();

    try {
    	
    	servir1.join();
    	comer1.join();
    	comer2.join();
    }
    catch(InterruptedException e) {
        Thread.currentThread().interrupt();

    } finally {
    	System.out.println("Se ha terminado");
	}
    }
}