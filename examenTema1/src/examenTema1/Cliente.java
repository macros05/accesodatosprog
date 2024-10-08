package examenTema1;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Cliente {

	private String nombre;
	private int edad;
	private String email;
	private String ciudad;
	
	public Cliente(String nombre, int edad, String email, String ciudad) {
		this.nombre = nombre;
		this.edad = edad;
		this.email = email;
		this.ciudad = ciudad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public static void leerConfiguracionyFiltrar(ArrayList<Cliente> clientes, String nombreFichero,
			String nombreArchivoClientes, int propiedadEdadMinima, int propiedadEdadMaxima, int inicioLinea,
			int finalLinea) {

		Properties configuracion = new Properties();

		try {
			configuracion.load(new FileInputStream(nombreFichero));
			nombreArchivoClientes = configuracion.getProperty("nombrefichero");
			propiedadEdadMinima = Integer.parseInt(configuracion.getProperty("edadmin"));
			propiedadEdadMaxima = Integer.parseInt(configuracion.getProperty("edadmax"));
			inicioLinea = Integer.parseInt(configuracion.getProperty("inicio"));
			finalLinea = Integer.parseInt(configuracion.getProperty("fin"));

		} catch (IOException e) {
			System.out.println("Error");
		}
		File archivo = new File(nombreArchivoClientes);
		Scanner lectorArchivo = null;
		int linea = 0;
		try {
			lectorArchivo = new Scanner(archivo);
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado");
		}

		while (lectorArchivo.hasNext()) {
			String[] datosCliente = lectorArchivo.nextLine().split(",");

			String nombre = datosCliente[0];
			int edad = Integer.parseInt(datosCliente[1]);
			String email = datosCliente[2];
			String ciudad = datosCliente[3];
			linea++;
			Cliente clienteLeido = new Cliente(nombre, edad, email, ciudad);
			if (linea >= inicioLinea && linea <= finalLinea && clienteLeido.getEdad() >= propiedadEdadMinima
					&& clienteLeido.getEdad() <= propiedadEdadMaxima) {
				clientes.add(clienteLeido);
			}
		}
		System.out.println("Estos son los clientes que cumplen los requisitos: " + clientes.toString());

	}

	public static void guardarProductosEnArchivo (ArrayList<Cliente> clientes, String archivo) {
		
		try {
			FileOutputStream fos = new FileOutputStream(archivo);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			for (Cliente cliente: clientes) {
				oos.writeUTF(cliente.getNombre());
				oos.write(cliente.getEdad());
				oos.writeUTF(cliente.getEmail());
				oos.writeUTF(cliente.getCiudad());
			}
			
			System.out.println("Lista de productos guardada correctamente en " + archivo);
			oos.close();
		}
				
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Cliente [nombre=" + nombre + ", edad=" + edad + ", email=" + email + ", ciudad=" + ciudad + "]\n";
	}
	
}