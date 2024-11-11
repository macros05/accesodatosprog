package processbuilder204;

public class Principal {
    public static void main(String[] args) {
        Saludo s = new Saludo();

        Personal Empleado1 = new Personal("Pepe", s, false);
        Personal Empleado2 = new Personal("José", s, false);
        Personal Empleado3 = new Personal("Pedro", s, false);
        Personal Jefe1 = new Personal("JEFE", s, true);

        Empleado1.start();
        Empleado2.start();
        Empleado3.start();
        Jefe1.start();
    }
}
