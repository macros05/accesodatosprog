package processbuilder204;

public class Personal extends Thread {
    private String nombre;
    private boolean esJefe;
    private Saludo saludo;

    public Personal(String nombre, Saludo saludo, boolean esJefe) {
        this.nombre = nombre;
        this.saludo = saludo;
        this.esJefe = esJefe;
    }

    @Override
    public void run() {
        saludo.esperarSaludo(nombre, esJefe);
    }
}
