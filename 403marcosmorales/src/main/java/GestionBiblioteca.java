import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionBiblioteca {
	private static final String DB_NAME = "biblioteca";
	private static final MongoClient mongoClient = MongoClients
			.create("mongodb://usuario_biblioteca:1234@143.47.47.116:27017/biblioteca");
	private static final MongoDatabase database = mongoClient.getDatabase(DB_NAME);
	private static final MongoCollection<Document> librosCollection = database.getCollection("libros");
	private static final MongoCollection<Document> autoresCollection = database.getCollection("autores");
	private static final MongoCollection<Document> sociosCollection = database.getCollection("socios");

	private static final Scanner scanner = new Scanner(System.in);

	public static void agregarAutor() {
		System.out.print("Ingrese el nombre del autor: ");
		String nombre = scanner.nextLine();
		System.out.print("Ingrese la nacionalidad del autor: ");
		String nacionalidad = scanner.nextLine();

		Document autor = new Document("nombre", nombre).append("nacionalidad", nacionalidad);
		autoresCollection.insertOne(autor);
		System.out.println("✅ Autor agregado: " + nombre);
	}

	public static void agregarLibro() {
		System.out.print("Ingrese el título del libro: ");
		String titulo = scanner.nextLine();
		System.out.print("Ingrese el ISBN del libro: ");
		String isbn = scanner.nextLine();
		System.out.print("Ingrese el nombre del autor del libro: ");
		String autorNombre = scanner.nextLine();
		System.out.print("Ingrese el género del libro: ");
		String genero = scanner.nextLine();

		Document autor = autoresCollection
				.find(new Document("nombre", new Document("$regex", "^" + autorNombre + "$").append("$options", "i")))
				.first();

		if (autor == null) {
			System.out.println("❌ Autor no encontrado. Agregue el autor antes de añadir el libro.");
			return;
		}

		Document libro = new Document().append("titulo", titulo).append("isbn", isbn)
				.append("autor_id", autor.getObjectId("_id")).append("genero", genero).append("disponible", true);

		librosCollection.insertOne(libro);
		System.out.println("✅ Libro agregado: " + titulo);
	}

	public static void agregarSocio() {
		System.out.print("Ingrese el nombre del socio: ");
		String nombre = scanner.nextLine();
		System.out.print("Ingrese la dirección del socio: ");
		String direccion = scanner.nextLine();

		Document socio = new Document("nombre", nombre).append("direccion", direccion).append("libros_prestados",
				new ArrayList<ObjectId>());
		sociosCollection.insertOne(socio);
		System.out.println("✅ Socio agregado: " + nombre);
	}

	private static Document seleccionarSocio(String nombreSocio) {
		List<Document> socios = sociosCollection
				.find(new Document("nombre", new Document("$regex", "^" + nombreSocio + "$").append("$options", "i")))
				.into(new ArrayList<>());

		if (socios.isEmpty()) {
			System.out.println("❌ No se encontró ningún socio con ese nombre.");
			return null;
		} else if (socios.size() == 1) {
			return socios.get(0);
		} else {
			System.out.println("🔍 Se encontraron varios socios con el mismo nombre. Seleccione uno:");
			for (int i = 0; i < socios.size(); i++) {
				System.out.println((i + 1) + ". " + socios.get(i).getString("nombre") + " - "
						+ socios.get(i).getString("direccion"));
			}

			System.out.print("Ingrese el número del socio correspondiente: ");
			int opcion = Integer.parseInt(scanner.nextLine()) - 1;

			if (opcion < 0 || opcion >= socios.size()) {
				System.out.println("❌ Selección inválida.");
				return null;
			}
			return socios.get(opcion);
		}
	}

	public static void prestarLibro() {
		System.out.print("Ingrese el título del libro a prestar: ");
		String tituloLibro = scanner.nextLine();
		System.out.print("Ingrese el nombre del socio: ");
		String nombreSocio = scanner.nextLine();

		Document libro = librosCollection
				.find(new Document("titulo", new Document("$regex", "^" + tituloLibro + "$").append("$options", "i"))
						.append("disponible", true))
				.first();

		Document socio = seleccionarSocio(nombreSocio);
		if (libro == null || socio == null) {
			System.out.println("❌ Libro no disponible o socio no encontrado.");
			return;
		}

		librosCollection.updateOne(new Document("_id", libro.getObjectId("_id")),
				new Document("$set", new Document("disponible", false)));
		sociosCollection.updateOne(new Document("_id", socio.getObjectId("_id")),
				new Document("$push", new Document("libros_prestados", libro.getObjectId("_id"))));

		System.out.println("✅ Libro prestado: " + tituloLibro + " a " + socio.getString("nombre"));
	}

	public static void devolverLibro() {
		System.out.print("Ingrese el título del libro a devolver: ");
		String tituloLibro = scanner.nextLine();
		System.out.print("Ingrese el nombre del socio: ");
		String nombreSocio = scanner.nextLine();

		Document libro = librosCollection
				.find(new Document("titulo", new Document("$regex", "^" + tituloLibro + "$").append("$options", "i")))
				.first();

		Document socio = seleccionarSocio(nombreSocio);
		if (libro == null || socio == null) {
			System.out.println("❌ Libro o socio no encontrado.");
			return;
		}

		librosCollection.updateOne(new Document("_id", libro.getObjectId("_id")),
				new Document("$set", new Document("disponible", true)));
		sociosCollection.updateOne(new Document("_id", socio.getObjectId("_id")),
				new Document("$pull", new Document("libros_prestados", libro.getObjectId("_id"))));

		System.out.println("✅ Libro devuelto: " + tituloLibro);
	}

	public static void listarLibrosDisponibles() {
		FindIterable<Document> libros = librosCollection.find(new Document("disponible", true));
		System.out.println("\n📚 Libros disponibles:");
		for (Document libro : libros) {
			System.out.println("- " + libro.getString("titulo"));
		}
	}

	public static void main(String[] args) {
		mostrarMenu();
	}

	public static void mostrarMenu() {
		while (true) {
			System.out.println("\n📖 GESTIÓN DE BIBLIOTECA 📖");
			System.out.println("1️⃣ - Agregar Autor");
			System.out.println("2️⃣ - Agregar Libro");
			System.out.println("3️⃣ - Agregar Socio");
			System.out.println("4️⃣ - Prestar Libro");
			System.out.println("5️⃣ - Devolver Libro");
			System.out.println("6️⃣ - Listar Libros Disponibles");
			System.out.println("0️⃣ - Salir");
			System.out.print("Seleccione una opción: ");

			String opcion = scanner.nextLine();
			switch (opcion) {
			case "1":
				agregarAutor();
				break;
			case "2":
				agregarLibro();
				break;
			case "3":
				agregarSocio();
				break;
			case "4":
				prestarLibro();
				break;
			case "5":
				devolverLibro();
				break;
			case "6":
				listarLibrosDisponibles();
				break;
			case "0":
				System.out.println("Saliendo del sistema...");
				mongoClient.close();
				scanner.close();
				return;
			default:
				System.out.println("❌ Opción inválida. Inténtelo de nuevo.");
			}
		}
	}
}