package examenTema2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class viajes {

    public static int idViaje(String destino, Connection con) {
        int idViaje = 0;
        String query = "Select idviajes from viajes where destino = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, destino);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idViaje = rs.getInt("idviajes");
            }
        } catch (SQLException e) {
            return -1;
        }
        if (idViaje == 0) {
        	return -1;
        }
        return idViaje;
    }

    public static int plazasLibres(String destino, Connection con) {
        String query = "Select totalplazas from viajes where destino = ?";
        int plazasLibres = 0;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, destino);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	plazasLibres = rs.getInt(1);
            }
        } catch (SQLException e) {
            return -1;
        }

        return plazasLibres;
    }

    public static void altaViajes(Connection con, Scanner scanner)  {
        System.out.println("Introduce el destino:");
        String destino = scanner.next();
        String query = "Select destino from viajes where destino = ?";
        boolean existe = false;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, destino);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }

        if (!existe) {
            System.out.println("Introduzca el total de plazas");
            int totalPlazas = scanner.nextInt();
            String sql = "INSERT INTO viajes(destino, totalplazas) VALUES(?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, destino);
                pstmt.setInt(2, totalPlazas);
                pstmt.executeUpdate();
                System.out.println("Viaje añadido correctamente.");
            }
            catch (SQLException e) {
            	e.printStackTrace();
            }
        } else {
            System.out.println("El destino ya existe");
        }
    }

    public static void crearReserva(Connection con, Scanner scanner) throws SQLException {
        System.out.println("Introduzca su destino: ");
        String destino = scanner.next();
        int idViaje = idViaje(destino, con);
        System.out.println("Introduzca el código del cliente");
        int codigoCliente = scanner.nextInt();
        System.out.println("Introduzca el número de plazas que quiere");
        int numeroPlazas = scanner.nextInt();
        int plazasLibres = plazasLibres(destino, con);
        String estado = (plazasLibres < numeroPlazas) ? "E" : "A";

        if (idViaje == -1) {
            System.out.println("El destino no existe, no se crea reserva");
        } else {
            String sql = "INSERT INTO reservas(idcliente, fecha, plazas, estado, numviaje) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, codigoCliente);
                pstmt.setDate(2, Date.valueOf(LocalDate.now()));
                pstmt.setInt(3, numeroPlazas);
                pstmt.setString(4, estado);
                pstmt.setInt(5, idViaje);
                pstmt.executeUpdate();
                System.out.println("Reserva añadida correctamente.");
            }
            if (estado.equals("A")) {
                String updateQuery = "UPDATE viajes SET totalplazas = totalplazas - ? WHERE idviajes = ?";
                try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
                    stmt.setInt(1, numeroPlazas);
                    stmt.setInt(2, idViaje);
                    stmt.executeUpdate();
                }
            }
        }
    }

    public static void cancelarReserva(Connection con, Scanner scanner) {
        System.out.println("Introduzca el número de reserva");
        int numeroReserva = scanner.nextInt();
        System.out.println("Introduzca su número de cliente");
        int numeroCliente = scanner.nextInt();

        String updateQuery = "UPDATE reservas SET estado = 'C' WHERE idreservas = ? AND idcliente = ?";
        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setInt(1, numeroReserva);
            stmt.setInt(2, numeroCliente);
            stmt.executeUpdate();
            System.out.println("Se ha cancelado la reserva");
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }
        String buscarPlazas = "SELECT plazas, numviaje FROM reservas WHERE idreservas = ? AND idcliente = ?";
        int plazasReservadas = 0, numeroViaje = 0;

        try (PreparedStatement stmt = con.prepareStatement(buscarPlazas)) {
            stmt.setInt(1, numeroReserva);
            stmt.setInt(2, numeroCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                plazasReservadas = rs.getInt(1);
                numeroViaje = rs.getInt(2);
            }
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }

        String actualizarPlazas = "UPDATE viajes SET totalplazas = totalplazas + ? WHERE idviajes = ?";
        try (PreparedStatement stmt = con.prepareStatement(actualizarPlazas)) {
            stmt.setInt(1, plazasReservadas);
            stmt.setInt(2, numeroViaje);
            stmt.executeUpdate();
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
    }
}
