package moralesmarcos102;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Vehiculo {
	
	private String matricula;
	private String marca;
	private String modelo;
	private String tipo;
	private double kilometros;
	
	public Vehiculo (String matricula, String marca, String modelo, String tipo, double kilometros) {
		
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.tipo = tipo;
		this.kilometros = kilometros;
		
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getKilometros() {
		return kilometros;
	}

	public void setKilometros(double kilometros) {
		this.kilometros = kilometros;
	}
	
	public static void leerVehiculos(ArrayList <Vehiculo> listaVehiculos) {
		File archivo = new File("vehiculos.txt");
		Scanner lectorVehiculos = null;
		
		try {
			lectorVehiculos = new Scanner(archivo);
		}
		catch(FileNotFoundException e) {
			System.out.println("Archivo no encontrado");
		}
		
		while(lectorVehiculos.hasNext()) {
			
			String[] datosVehiculo= lectorVehiculos.nextLine().split(";");

			String matricula = datosVehiculo[0];
			String marca = datosVehiculo[1];
			String modelo = datosVehiculo[2];
			String tipo = datosVehiculo[3];
			double kilometros = Double.parseDouble(datosVehiculo[4]);
			Vehiculo vehiculoLeido = new Vehiculo(matricula,marca,modelo,tipo,kilometros);
			
			listaVehiculos.add(vehiculoLeido);
			
			System.out.println(vehiculoLeido.toString());
		}
	}

	public static void ensenyarNumeroVehiculos(ArrayList<Vehiculo> listaVehiculos) {

		System.out.println("El numero de vehiculos es: " + listaVehiculos.size());

	}
	
	public static void buscarVehiculo (ArrayList <Vehiculo> listaVehiculos, String matricula) {
		boolean encontrado = false;
		
		for (Vehiculo vehiculo1: listaVehiculos) {
			
			if(vehiculo1.getMatricula().equalsIgnoreCase(matricula)) {
				System.out.println(vehiculo1.toString());
				 encontrado = true;
			}
		}
		if (!encontrado) {
			System.out.println("No hay vehiculos con la matricula: " + matricula);
		}
	}
	@Override
	public String toString() {
		return "Vehiculo [matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", tipo=" + tipo
				+ ", kilometros=" + kilometros + "]";
	}

}
