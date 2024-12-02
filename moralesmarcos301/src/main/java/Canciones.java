import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "canciones")  
public class Canciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     
    private int codigo;

    private String nombre;
    private String cantante;
    private String album;
    private LocalDate fechaLanzamiento;

    public Canciones() {}

    public Canciones(String nombre, String cantante, String album, LocalDate fechaLanzamiento) {
        this.nombre = nombre;
        this.cantante = cantante;
        this.album = album;
        this.fechaLanzamiento = fechaLanzamiento;
    }
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCantante() {
		return cantante;
	}

	public void setCantante(String cantante) {
		this.cantante = cantante;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public LocalDate getFechaLanzamiento() {
		return fechaLanzamiento;
	}

	public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
		this.fechaLanzamiento = fechaLanzamiento;
	}

	@Override
	public String toString() {
		return "Canciones [codigo=" + codigo + ", nombre=" + nombre + ", cantante=" + cantante + ", album=" + album
				+ ", fechaLanzamiento=" + fechaLanzamiento + "]";
	}
	
	
}
