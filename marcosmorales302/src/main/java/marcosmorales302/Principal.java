package marcosmorales302;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        while (true) {
            System.out.println("Menú de opciones:");
            System.out.println("1. Alta de cantantes");
            System.out.println("2. Consulta por género musical");
            System.out.println("3. Listado de todos los cantantes");
            System.out.println("4. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            switch (opcion) {
            case 1:
                altaCantante(session, scanner);
                break;
            case 2:
                consultaPorGenero(session, scanner);
                break;
            case 3:
                listarCantantes(session);
                break;
            case 4:
                session.close();
                factory.close();
                System.out.println("Programa finalizado.");
                return;
            default:
                System.out.println("Opción no válida.");
                break;
        }

        }
    }

    private static void altaCantante(Session session, Scanner scanner) {
        System.out.print("Nombre del cantante: ");
        String nombre = scanner.nextLine();

        System.out.print("ID del género musical: ");
        int generoId = scanner.nextInt();

        Transaction tx = session.beginTransaction();
        GeneroMusical genero = session.get(GeneroMusical.class, generoId);
        if (genero != null) {
            Cantante cantante = new Cantante();
            cantante.setNombre(nombre);
            cantante.setGenero(genero);
            session.persist(cantante);
            tx.commit();
            System.out.println("Cantante registrado con éxito.");
        } else {
            System.out.println("Género musical no encontrado.");
        }
    }

    private static void consultaPorGenero(Session session, Scanner scanner) {
        System.out.print("ID del género musical: ");
        int generoId = scanner.nextInt();

        GeneroMusical genero = session.get(GeneroMusical.class, generoId);
        if (genero != null) {
            List<Cantante> cantantes = genero.getCantantes();
            System.out.println("Cantantes del género " + genero.getNombre() + ":");
            for (Cantante c : cantantes) {
                System.out.println("- " + c.getNombre());
            }
        } else {
            System.out.println("Género musical no encontrado.");
        }
    }

    private static void listarCantantes(Session session) {
        List<Cantante> cantantes = session.createQuery("from Cantante", Cantante.class).list();
        System.out.println("Listado de cantantes:");
        for (Cantante c : cantantes) {
            System.out.println("ID: " + c.getId() + ", Nombre: " + c.getNombre() + ", Género: " + c.getGenero().getNombre());
        }
    }
}
