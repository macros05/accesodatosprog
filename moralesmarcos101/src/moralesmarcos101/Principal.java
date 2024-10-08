package moralesmarcos101;

import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {
		
		Scanner teclado = new Scanner(System.in);
		
		int opcion = 1;
		int opcion2 = 0;
		
		String matricula;
		String marca;
		String modelo;
		String tipo;
		double kilometros;
		
		ArrayList <Vehiculo> listaVehiculos = new ArrayList <> ();
		
		while (opcion == 1) {
			System.out.println("Introduzca la matricula del coche");
			matricula = teclado.next();
			
			System.out.println("Introduzca la marca del vehiculo");
			marca = teclado.next();
			
			System.out.println("Introduzca el modelo del vehiculo");
			modelo = teclado.next();
			
			System.out.println("Introduzca el tipo del vehiculo");
			tipo = teclado.next();
			
			System.out.println("Introduzca los kilometros del vehiculo");
			kilometros = teclado.nextDouble();
			
			System.out.println("Introduzca 0 si quiere exportar el vehiculo o otro numero si quiere introducir de nuevo los datos o salir");
			opcion2 = teclado.nextInt();
			if (opcion2 == 0) {
				Vehiculo.escribirArchivo(matricula, marca, modelo, tipo,kilometros, listaVehiculos);
			}
			System.out.println("Introduzca 1 si quiere introducir otro vehiculo, si quiere salir introduzca cualquier numero");
			opcion = teclado.nextInt();
			
		}
		
		System.out.println("Saliendo del programa");
		teclado.close();
	}
	

}
