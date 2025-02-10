import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class MongoDBCrud {

    private static final String CONNECTION_STRING = "mongodb://usuario_espectaculos:1234@143.47.47.116:27017";
    private static final String DATABASE_NAME = "espectaculos";
    private static MongoDatabase database;

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            database = mongoClient.getDatabase(DATABASE_NAME);
            Scanner scanner = new Scanner(System.in);
            int option;

            // Autenticación de usuario
            if (!autenticarUsuario(scanner)) {
                System.out.println("Autenticación fallida. Saliendo...");
                return;
            }

            do {
                System.out.println("\n--- Menú de Venta de Entradas ---");
                System.out.println("1. Crear talonario de entradas");
                System.out.println("2. Ver entradas disponibles");
                System.out.println("3. Comprar entradas");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (option) {
                    case 1:
                        crearTalonario(scanner);
                        break;
                    case 2:
                        verEntradasDisponibles(scanner);
                        break;
                    case 3:
                        comprarEntradas(scanner);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (option != 0);
        }
    }

    // Método de autenticación
    private static boolean autenticarUsuario(Scanner scanner) {
        System.out.print("Nombre de usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        MongoCollection<Document> coleccionUsuarios = database.getCollection("usuarios");

        // Buscar usuario y comparar la contraseña cifrada
        Document usuarioDB = coleccionUsuarios.find(Filters.eq("nombre", usuario)).first();

        if (usuarioDB != null) {
            String hashedPassword = usuarioDB.getString("contrasena");
            return BCrypt.checkpw(contrasena, hashedPassword);
        }

        return false;
    }

    // Crear talonario de entradas
    private static void crearTalonario(Scanner scanner) {
        System.out.print("Nombre del evento: ");
        String evento = scanner.nextLine();
        System.out.print("Número de entradas: ");
        int entradas = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        // Crear colección con el nombre del evento
        MongoCollection<Document> collection = database.getCollection(evento);

        // Crear entradas
        for (int i = 1; i <= entradas; i++) {
            Document entrada = new Document("numero_entrada", i)
                    .append("observaciones", "")
                    .append("nombre_cliente", null); // Entrada sin vender
            collection.insertOne(entrada);
        }

        System.out.println("Talonario creado con " + entradas + " entradas para el evento: " + evento);
    }

    // Ver entradas disponibles en todos los eventos
    private static void verEntradasDisponibles(Scanner scanner) {
        MongoIterable<String> colecciones = database.listCollectionNames();
        System.out.println("\nEventos disponibles:");

        for (String evento : colecciones) {
            if (!evento.equals("usuarios")) { // Excluir la colección de usuarios
                System.out.println("- " + evento);
            }
        }

        System.out.print("Elige un evento: ");
        String eventoSeleccionado = scanner.nextLine();

        MongoCollection<Document> collection = database.getCollection(eventoSeleccionado);

        // Consultar entradas disponibles
        long entradasDisponibles = collection.countDocuments(Filters.eq("nombre_cliente", null));
        System.out.println("Entradas disponibles para el evento " + eventoSeleccionado + ": " + entradasDisponibles);
    }

    // Comprar entradas para un evento
    private static void comprarEntradas(Scanner scanner) {
        System.out.print("Nombre del evento: ");
        String evento = scanner.nextLine();

        MongoCollection<Document> collection = database.getCollection(evento);

        // Verificar las entradas disponibles
        long entradasDisponibles = collection.countDocuments(Filters.eq("nombre_cliente", null));
        System.out.println("Entradas disponibles para " + evento + ": " + entradasDisponibles);

        if (entradasDisponibles == 0) {
            System.out.println("No hay entradas disponibles para este evento.");
            return;
        }

        System.out.print("¿Cuántas entradas desea comprar? ");
        int entradasAComprar = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (entradasAComprar > entradasDisponibles) {
            System.out.println("No hay suficientes entradas disponibles.");
            return;
        }

        // Asignar entradas
        for (int i = 0; i < entradasAComprar; i++) {
            collection.updateOne(Filters.eq("nombre_cliente", null),
                    Updates.set("nombre_cliente", "Cliente")); // Asigna el nombre del cliente
        }

        // Mostrar entradas restantes
        long entradasRestantes = collection.countDocuments(Filters.eq("nombre_cliente", null));
        System.out.println("Entradas restantes: " + entradasRestantes);
    }
}
