package moralesmarcos201;

import java.sql.*;
import java.util.Scanner;

public class Principal {
    private static final String URL = "jdbc:sqlite:agenda.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                crearTablaSiNoExiste(conn);
            }

            Scanner scanner = new Scanner(System.in);
            boolean salir = false;

            while (!salir) {
                System.out.println("Selecciona una opción:");
                System.out.println("1. Alta en la agenda");
                System.out.println("2. Consultar en la agenda");
                System.out.println("3. Salir");
                
                int opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {
                    case 1:
                        altaEnAgenda(conn, scanner);
                        break;
                    case 2:
                        consultarEnAgenda(conn, scanner);
                        break;
                    case 3:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void crearTablaSiNoExiste(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS contactos (\n"
                   + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                   + " nombre TEXT NOT NULL,\n"
                   + " apellido TEXT NOT NULL,\n"
                   + " email TEXT NOT NULL\n"
                   + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void altaEnAgenda(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Introduce el nombre:");
        String nombre = scanner.nextLine();

        System.out.println("Introduce el apellido:");
        String apellido = scanner.nextLine();

        System.out.println("Introduce el email:");
        String email = scanner.nextLine();

        String sql = "INSERT INTO contactos(nombre, apellido, email) VALUES(?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("Contacto añadido correctamente.");
        }
    }

    private static void consultarEnAgenda(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Introduce el nombre para buscar:");
        String nombre = scanner.nextLine();

        String sql = "SELECT * FROM contactos WHERE nombre = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {  
                System.out.println("No se encontró ningún contacto con ese nombre.");
            } else {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nombre: " + rs.getString("nombre"));
                    System.out.println("Apellido: " + rs.getString("apellido"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("---------------");
                }
            }
        }
    }
}
