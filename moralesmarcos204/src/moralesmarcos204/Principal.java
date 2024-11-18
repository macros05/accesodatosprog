package moralesmarcos204;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Principal {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/transaccionesjava";
    private static final String DB_USER = "usuario_java";
    private static final String DB_PASSWORD = "java123";

    public static void main(String[] args) {
        Connection con = null;
        Scanner scanner = new Scanner(System.in);

        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            con.setAutoCommit(false); // Iniciar la transacción

            Statement stmt = con.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS articulos (" +
                    "codart INT PRIMARY KEY, " +
                    "descripcion VARCHAR(255), " +
                    "existencias INT, " +
                    "precio DECIMAL(10, 2))");

            stmt.execute("CREATE TABLE IF NOT EXISTS pedidos (" +
                    "codped INT PRIMARY KEY, " +
                    "fecha DATE, " +
                    "codcli INT, " +
                    "direccion VARCHAR(255), " +
                    "codart INT, " +
                    "cantidad INT, " +
                    "FOREIGN KEY (codart) REFERENCES articulos(codart))");

            stmt.execute("CREATE TABLE IF NOT EXISTS envios (" +
                    "codped INT, " +
                    "codrider INT, " +
                    "terminado CHAR(1), " +
                    "FOREIGN KEY (codped) REFERENCES pedidos(codped), " +
                    "FOREIGN KEY (codrider) REFERENCES riders(codigo))");

            stmt.execute("CREATE TABLE IF NOT EXISTS riders (" +
                    "codigo INT PRIMARY KEY, " +
                    "nombre VARCHAR(255), " +
                    "disponible CHAR(1))");

            int opcion = 1;

            while (opcion != 0) {
                System.out.println("Introduzca 1 para introducir un pedido, 2 para introducir un rider, 0 para salir");
                opcion = scanner.nextInt();

                if (opcion == 1) {
                    try {
                        // Solicitar datos al usuario para insertar en la tabla de artículos
                        System.out.println("Ingrese los datos del artículo:");
                        System.out.print("Código de artículo: ");
                        int codart = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.print("Descripción: ");
                        String descripcion = scanner.nextLine();
                        System.out.print("Existencias: ");
                        int existencias = scanner.nextInt();
                        System.out.print("Precio: ");
                        double precio = scanner.nextDouble();
                        scanner.nextLine(); 

                        PreparedStatement pstmtArticulo = con.prepareStatement(
                                "INSERT INTO articulos (codart, descripcion, existencias, precio) VALUES (?, ?, ?, ?)");
                        pstmtArticulo.setInt(1, codart);
                        pstmtArticulo.setString(2, descripcion);
                        pstmtArticulo.setInt(3, existencias);
                        pstmtArticulo.setDouble(4, precio);
                        pstmtArticulo.executeUpdate();

                        // Solicitar datos al usuario para la transacción del pedido
                        System.out.println("\nIngrese los datos del pedido:");
                        System.out.print("Código de pedido: ");
                        int codped = scanner.nextInt();
                        scanner.nextLine();  
                        System.out.print("Fecha del pedido (YYYY-MM-DD): ");
                        String fecha = scanner.nextLine();
                        System.out.print("Código del cliente: ");
                        int codcli = scanner.nextInt();
                        scanner.nextLine(); 
                        System.out.print("Dirección: ");
                        String direccion = scanner.nextLine();
                        System.out.print("Código de artículo: ");
                        int codartPedido = scanner.nextInt();
                        System.out.print("Cantidad: ");
                        int cantidad = scanner.nextInt();
                        scanner.nextLine(); 

                        // Alta en tabla de pedidos
                        PreparedStatement pstmtPedido = con.prepareStatement(
                                "INSERT INTO pedidos (codped, fecha, codcli, direccion, codart, cantidad) VALUES (?, ?, ?, ?, ?, ?)");
                        pstmtPedido.setInt(1, codped);
                        pstmtPedido.setString(2, fecha);
                        pstmtPedido.setInt(3, codcli);
                        pstmtPedido.setString(4, direccion);
                        pstmtPedido.setInt(5, codartPedido);
                        pstmtPedido.setInt(6, cantidad);
                        pstmtPedido.executeUpdate();

                        // Actualización de existencias en tabla de artículos
                        PreparedStatement pstmtUpdateArticulo = con.prepareStatement(
                                "UPDATE articulos SET existencias = existencias - ? WHERE codart = ?");
                        pstmtUpdateArticulo.setInt(1, cantidad);
                        pstmtUpdateArticulo.setInt(2, codartPedido);
                        pstmtUpdateArticulo.executeUpdate();

                        // Alta en tabla de envíos
                        System.out.print("\nIngrese el código del rider asignado para el envío: ");
                        int codriderEnvio = scanner.nextInt();
                        scanner.nextLine(); 
                        PreparedStatement pstmtEnvio = con.prepareStatement(
                                "INSERT INTO envios (codped, codrider, terminado) VALUES (?, ?, 'N')");
                        pstmtEnvio.setInt(1, codped);
                        pstmtEnvio.setInt(2, codriderEnvio);
                        pstmtEnvio.executeUpdate();

                        // Finalizar la transacción
                        con.commit();
                        System.out.println("Transacción completada exitosamente.");
                    } catch (SQLException e) {
                        // En caso de error, deshacer los cambios
                        con.rollback();
                        System.out.println("Transacción fallida. Se ha ejecutado rollback.");
                        e.printStackTrace();
                    }
                }

                if (opcion == 2) {
                    // Solicitar datos al usuario para insertar en la tabla de riders
                    System.out.println("\nIngrese los datos del rider:");
                    System.out.print("Código de rider: ");
                    int codigoRider = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    System.out.print("Nombre del rider: ");
                    String nombreRider = scanner.nextLine();
                    System.out.print("Disponible (S/N): ");
                    String disponible = scanner.nextLine();

                    try {
                        PreparedStatement pstmtRider = con.prepareStatement(
                                "INSERT INTO riders (codigo, nombre, disponible) VALUES (?, ?, ?)");
                        pstmtRider.setInt(1, codigoRider);
                        pstmtRider.setString(2, nombreRider);
                        pstmtRider.setString(3, disponible);
                        pstmtRider.executeUpdate();
                    } catch (SQLException e) {
                        // En caso de error al insertar rider
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            scanner.close();
        }
        System.out.println("Saliendo del programa");
    }
}
