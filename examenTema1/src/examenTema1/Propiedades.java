package examenTema1;

public class Propiedades {
	private String nombreArchivoClientes; 
	private int propiedadEdadMinima;
	private int propiedadEdadMaxima; 
	private int inicioLinea;
	private int finalLinea;
	
	public Propiedades (String nombreArchivoClientes, int propiedadEdadMinima, int propiedadEdadMaxima, int inicioLinea, int finalLinea) {
		
		this.nombreArchivoClientes = nombreArchivoClientes;
		this.propiedadEdadMinima = propiedadEdadMinima;
		this.propiedadEdadMaxima = propiedadEdadMaxima;
		this.inicioLinea = inicioLinea;
		this.finalLinea = finalLinea;
	}
}
