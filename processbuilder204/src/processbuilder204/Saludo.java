package processbuilder204;

public class Saludo {
    private boolean jefeSaludo = false;

    public synchronized void esperarSaludo(String nombre, boolean esJefe) {
        if (esJefe) {
            System.out.println("****** " + nombre + "-: Buenos días empleados. ******");
            jefeSaludo = true;
            notifyAll(); // Notifica a todos los empleados para que puedan continuar
        } else {
            System.out.println(nombre + " llegó.");
            while (!jefeSaludo) {
                try {
                    wait(); // Empleado espera hasta que el jefe salude
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(nombre.toUpperCase() + "-: Buenos días jefe.");
        }
    }
}
