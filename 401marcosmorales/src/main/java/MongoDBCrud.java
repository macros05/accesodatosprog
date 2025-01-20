import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.Scanner;

public class MongoDBCrud {
    private static final String CONNECTION_STRING = "mongodb://usuario:password@localhost:27017";
    private static final String DATABASE_NAME = "miBaseDeDatos";
    private static final String COLLECTION_NAME = "libros";

    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);

            Scanner scanner = new Scanner(System.in);
            int option;

            do {
                System.out.println("\n--- Menú CRUD ---");
                System.out.println("1. Crear un documento");
                System.out.println("2. Consultar un documento");
                System.out.println("3. Consultar múltiples documentos");
                System.out.println("4. Actualizar un documento");
                System.out.println("5. Eliminar documentos");
                System.out.println("6. Contar documentos");
                System.out.println("7. Buscar libros por rango de años");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (option) {
                    case 1:
                        crearDocumento(scanner);
                        break;
                    case 2:
                        consultarDocumento(scanner);
                        break;
                    case 3:
                        consultarMultiplesDocumentos();
                        break;
                    case 4:
                        actualizarDocumento(scanner);
                        break;
                    case 5:
                        eliminarDocumentos(scanner);
                        break;
                    case 6:
                        contarDocumentos();
                        break;
                    case 7:
                        buscarPorRangoDeAnios(scanner);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } while (option != 0);
        }
    }

    private static void crearDocumento(Scanner scanner) {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Año: ");
        int anio = scanner.nextInt();

        Document libro = new Document("titulo", titulo)
                .append("autor", autor)
                .append("anio", anio);
        collection.insertOne(libro);
        System.out.println("Documento creado correctamente.");
    }

    private static void consultarDocumento(Scanner scanner) {
        System.out.print("Título del libro a buscar: ");
        String titulo = scanner.nextLine();

        Document libro = collection.find(Filters.eq("titulo", titulo)).first();
        if (libro != null) {
            System.out.println("Documento encontrado: " + libro.toJson());
        } else {
            System.out.println("No se encontró un libro con ese título.");
        }
    }

    private static void consultarMultiplesDocumentos() {
        FindIterable<Document> libros = collection.find();
        System.out.println("Documentos en la colección:");
        for (Document libro : libros) {
            System.out.println(libro.toJson());
        }
    }

    private static void actualizarDocumento(Scanner scanner) {
        System.out.print("Título del libro a actualizar: ");
        String titulo = scanner.nextLine();
        System.out.print("Nuevo autor: ");
        String nuevoAutor = scanner.nextLine();

        collection.updateOne(Filters.eq("titulo", titulo), Updates.set("autor", nuevoAutor));
        System.out.println("Documento actualizado correctamente.");
    }

    private static void eliminarDocumentos(Scanner scanner) {
        System.out.print("Título del libro a eliminar (o presiona Enter para eliminar todos): ");
        String titulo = scanner.nextLine();

        if (titulo.isEmpty()) {
            collection.deleteMany(new Document());
            System.out.println("Todos los documentos han sido eliminados.");
        } else {
            collection.deleteOne(Filters.eq("titulo", titulo));
            System.out.println("Documento eliminado correctamente.");
        }
    }

    private static void contarDocumentos() {
        long count = collection.countDocuments();
        System.out.println("Número total de documentos en la colección: " + count);
    }

    private static void buscarPorRangoDeAnios(Scanner scanner) {
        System.out.print("Año mínimo: ");
        int anioMin = scanner.nextInt();
        System.out.print("Año máximo: ");
        int anioMax = scanner.nextInt();

        FindIterable<Document> libros = collection.find(Filters.and(
                Filters.gte("anio", anioMin),
                Filters.lte("anio", anioMax))
        );

        System.out.println("Libros encontrados:");
        for (Document libro : libros) {
            System.out.println(libro.toJson());
        }
    }
}
