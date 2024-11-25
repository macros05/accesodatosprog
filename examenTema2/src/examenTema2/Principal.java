package examenTema2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Principal {
	
	 private static final String DB_URL = "jdbc:mysql://localhost:3306/examenjdbc";
	 private static final String DB_USER = "usuario_java";
	 private static final String DB_PASSWORD = "java123";
	    
	    
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		int opcion = 1;
		
        Connection con = null;
        try {
			con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			System.out.println("ERROR");
		}
		while (opcion != 0) {
		System.out.println("Introduzca el \n1. Para ver las plazas libres de un destino \n"
				+ "2. Para dar de alta un viaje \n"
				+ "3. Para crear una reserva \n"
				+ "4. Para cancelar una reserva \n"
				+ "0. Para salir del programa");
		opcion = scanner.nextInt();
		
			switch (opcion) {
				
			case 1:
				System.out.println("Introduzca el destino");
				String destino = scanner.next();
				
				System.out.println("Hay un total de " + viajes.plazasLibres(destino, con) + " plazas en " + destino);
				break;
			
			case 2:
					viajes.altaViajes(con, scanner);
				break;
			
			case 3:
				try {
					viajes.crearReserva(con, scanner);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			
			case 4:
				viajes.cancelarReserva(con, scanner);
				break;
			
			default:
				System.out.println("Ponga otra opción");
				break;
			case 0:
				break;
			}
		}
	}

}
