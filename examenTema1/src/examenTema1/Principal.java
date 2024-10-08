package examenTema1;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {


	public static void main(String[] args) {

		Scanner lector = new Scanner(System.in);
		ArrayList <Cliente> clientes = new ArrayList<>();
		
		System.out.println("Introduzca el nombre del archivo de propiedades");
		String nombreArchivo = lector.next();
		String archivoAExportar = "clienteBIN.dat";
		
		String nombreArchivoClientes = null;
		int propiedadEdadMinima = 0;
		int propiedadEdadMaxima = 0;
		int inicioLinea = 0;
		int finalLinea = 0;
		
		Cliente.leerConfiguracionyFiltrar(clientes, nombreArchivo, nombreArchivoClientes, propiedadEdadMinima,
				propiedadEdadMaxima, inicioLinea, finalLinea);
		
		Cliente.guardarProductosEnArchivo(clientes, archivoAExportar);
		
		lector.close();
	}
}
