package moralesmarcos202;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Principal {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/accesoadatos_tickets";
	private static final String DB_USER = "tickets";
	private static final String DB_PASSWORD = "tickets123";
	private static final int TICKETS_INICIALES = 100;

	private static int totalTicketsVendidos = 0; // Contador global para tickets vendidos

	private static int obtenerTicketsDisponibles(Connection conn) throws SQLException {
		String query = "SELECT disponibles FROM tickets WHERE id = 1 FOR UPDATE";
		try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("disponibles");
			}
			throw new SQLException("No se encontraron tickets");
		}
	}

	private static boolean actualizarTickets(Connection conn, int ticketsVendidos) throws SQLException {
		int disponibles = obtenerTicketsDisponibles(conn);
		if (disponibles >= ticketsVendidos) {
			String updateQuery = "UPDATE tickets SET disponibles = disponibles - ? WHERE id = 1";
			try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
				stmt.setInt(1, ticketsVendidos);
				stmt.executeUpdate();
				totalTicketsVendidos += ticketsVendidos; // Actualiza el total de tickets vendidos
				return true;
			}
		} else {
			System.out.println("No hay suficientes tickets para vender " + ticketsVendidos);
			return false;
		}
	}

	private static void agregarTickets(Connection conn, int ticketsAAgregar) throws SQLException {
		String updateQuery = "UPDATE tickets SET disponibles = disponibles + ? WHERE id = 1";
		try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
			stmt.setInt(1, ticketsAAgregar);
			stmt.executeUpdate();
			System.out.println("Se han añadido " + ticketsAAgregar + " tickets disponibles.");
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Seleccione una opción:");
			System.out.println("1. Comprar tickets");
			System.out.println("2. Añadir tickets disponibles");
			System.out.println("3. Salir");
			int opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				Runnable compraTicketTask = () -> {
					try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
						conn.setAutoCommit(false);
						int ticketsAComprar = (int) (Math.random() * 50) + 1;

						boolean ventaExitosa = actualizarTickets(conn, ticketsAComprar);
						if (ventaExitosa) {
							conn.commit();
							System.out.println("Tickets vendidos: " + ticketsAComprar);
						} else {
							conn.rollback();
						}

					} catch (SQLException e) {
						e.printStackTrace();
					}
				};

				Thread hilo1 = new Thread(compraTicketTask);
				Thread hilo2 = new Thread(compraTicketTask);
				Thread hilo3 = new Thread(compraTicketTask);

				hilo1.start();
				hilo2.start();
				hilo3.start();

				try {
					hilo1.join();
					hilo2.join();
					hilo3.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				int ticketsRestantes;
				try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
					ticketsRestantes = obtenerTicketsDisponibles(conn);
					System.out.println("Total de tickets vendidos: " + totalTicketsVendidos);
					System.out.println("Total de tickets disponibles: " + ticketsRestantes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 2:
				System.out.println("¿Cuántos tickets desea añadir?");
				int ticketsAAgregar = scanner.nextInt();

				try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
					agregarTickets(conn, ticketsAAgregar);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 3:
				System.out.println("Saliendo del programa...");
				scanner.close();
				return;

			default:
				System.out.println("Opción no válida.");
				break;
			}
		}
	}
}
