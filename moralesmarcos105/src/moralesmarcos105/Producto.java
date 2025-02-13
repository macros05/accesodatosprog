package moralesmarcos105;

import java.io.*;


public class Producto implements Serializable {
    private String articulo;
    private double precio;
    private int existencias;

    public Producto(String articulo, double precio, int existencias) {
        this.articulo = articulo;
        this.precio = precio;
        this.existencias = existencias;
    }

    public String getArticulo() {
        return articulo;
    }

    public double getPrecio() {
        return precio;
    }

    public int getExistencias() {
        return existencias;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "articulo='" + articulo + '\'' +
                ", precio=" + precio +
                ", existencias=" + existencias +
                '}';
    }
}