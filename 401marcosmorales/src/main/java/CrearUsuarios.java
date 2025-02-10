import org.mindrot.jbcrypt.BCrypt;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class CrearUsuarios {
    public static void main(String[] args) {
        // Conectar a MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://usuario_espectaculos:1234@143.47.47.116:27017");
        MongoDatabase database = mongoClient.getDatabase("espectaculos");
        MongoCollection<Document> coleccionUsuarios = database.getCollection("usuarios");

        // Crear un scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Solicitar nombre de usuario y contraseña
        System.out.print("Introduce el nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        
        System.out.print("Introduce la contraseña: ");
        String contrasena = scanner.nextLine();

        // Cifrar la contraseña
        String hashedPassword = BCrypt.hashpw(contrasena, BCrypt.gensalt());

        // Crear un documento de usuario con nombre y contraseña cifrada
        Document usuario = new Document("nombre", nombreUsuario)
                .append("contrasena", hashedPassword);

        // Insertar el usuario en la colección
        coleccionUsuarios.insertOne(usuario);

        System.out.println("Usuario creado correctamente");

        // Cerrar la conexión
        mongoClient.close();
    }
}
