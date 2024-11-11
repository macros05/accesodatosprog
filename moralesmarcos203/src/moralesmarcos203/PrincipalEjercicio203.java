package moralesmarcos203;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class PrincipalEjercicio203 {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ejercicio_java";
    private static final String DB_USER = "usuario_java";
    private static final String DB_PASSWORD = "java123";

    // Método para insertar un usuario
    public static void insertarUsuario(String nombreLogin, String contrasena, String nombreCompleto) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "{CALL InsertarUsuario(?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, nombreLogin);
                stmt.setString(2, contrasena);
                stmt.setString(3, nombreCompleto);
                stmt.executeUpdate();
                System.out.println("Usuario insertado correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    // Método para contar el número de entradas de un usuario
    public static int contarEntradasUsuario(String nombreLogin) {
        int numEntradas = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "{CALL ContarEntradasUsuario(?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, nombreLogin);
                stmt.registerOutParameter(2, java.sql.Types.INTEGER);
                stmt.execute();
                numEntradas = stmt.getInt(2);
                System.out.println("Número de entradas del usuario " + nombreLogin + ": " + numEntradas);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar entradas: " + e.getMessage());
        }
        return numEntradas;
    }
    // Método main para probar la funcionalidad
    public static void main(String[] args) {
        // Probar la inserción de un usuario
        String nombreLogin = "usrPrueba";
        String contrasena = "123";
        String nombreCompleto = "Usuario de Prueba";
        
        insertarUsuario(nombreLogin, contrasena, nombreCompleto);

        // Probar el conteo de entradas de un usuario
        int entradas = contarEntradasUsuario(nombreLogin);
        System.out.println("El usuario " + nombreLogin + " tiene " + entradas + " entrada(s) en la base de datos.");
    }
}
