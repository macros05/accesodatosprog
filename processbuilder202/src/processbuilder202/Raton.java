package processbuilder202;

public class Raton extends Thread {
    
    private String nombre;
    private int tiempocome;
    
    public Raton(String nombre, int tiempocome) {
        this.nombre = nombre;
        this.tiempocome = tiempocome;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempocome() {
        return tiempocome;
    }

    public void setTiempocome(int tiempocome) {
        this.tiempocome = tiempocome;
    }
    
    @Override
    public void run() {
        try {
            System.out.printf("%s empieza la merienda %n", this.getNombre());
            Thread.sleep(500 * this.getTiempocome());  
            System.out.printf("%s terminó de comer %n", this.getNombre());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
