package moralesmarcos101;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

	public static void escribirArchivo(String matricula, String marca, String modelo, String tipo, double kilometros, ArrayList <Vehiculo> listaVehiculos ) {
		Vehiculo vehiculo1 = new Vehiculo(matricula,marca,modelo,tipo, kilometros);
				
		listaVehiculos.add(vehiculo1);
		
		File archivoVehiculos = new File("vehiculos.txt");
		FileWriter fwVehiculos = null;
		
		try {
			fwVehiculos = new FileWriter(archivoVehiculos);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for (Vehiculo vehiculo2: listaVehiculos) {
			try {
				fwVehiculos.write(vehiculo2.getMatricula() + ";" + vehiculo2.getMarca() + ";" + vehiculo2.getModelo() + ";" + vehiculo2.getTipo() + ";" + vehiculo2.getKilometros() +"\n");
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Se ha importado el vehiculo con exito");
		try {
			fwVehiculos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
