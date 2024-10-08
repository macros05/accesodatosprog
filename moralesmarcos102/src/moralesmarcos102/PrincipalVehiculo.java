package moralesmarcos102;

import java.util.ArrayList;
import java.util.Scanner;

public class PrincipalVehiculo {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		ArrayList <Vehiculo>  listaVehiculos = new ArrayList<>();	
		
		int opcion = 0;
		String matricula = null;
		
		while (opcion >= 0 && opcion <= 2) {
			
			System.out.println("Introduzca 0 para exportar vehiculos y enseñarlos \n"
					+ "Introduzca 1 para ver cuantos vehiculos hay en la lista \n"
					+ "Introduzca 2 para buscar algun vehiculo \n"
					+ "Otro numero para salir");
			opcion = teclado.nextInt();
			
			switch(opcion) {
			
				case 0:
					Vehiculo.leerVehiculos(listaVehiculos);
					break;
				case 1:
					Vehiculo.ensenyarNumeroVehiculos(listaVehiculos);
					break;
				case 2:
					System.out.println("Introduzca la matricula");
					matricula = teclado.next();
					Vehiculo.buscarVehiculo(listaVehiculos, matricula);
				default: 
					break;
					
			}
			
		}
		System.out.println("Saliendo del programa");
		teclado.close();
	}

}
