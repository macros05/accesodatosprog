package moralesmarcos104;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Crear objetos Producto
        Producto p1 = new Producto("Camiseta", 19.99, 50);
        Producto p2 = new Producto("Pantalones", 39.99, 30);
        Producto p3 = new Producto("Zapatos", 59.99, 20);

        // Almacenarlos en una lista
        List<Producto> listaProductos = new ArrayList<>();
        listaProductos.add(p1);
        listaProductos.add(p2);
        listaProductos.add(p3);

        // Grabar los productos en un fichero binario
        try (FileOutputStream fos = new FileOutputStream("productos.bin");
             DataOutputStream dos = new DataOutputStream(fos)) {

            for (Producto producto : listaProductos) {
                dos.writeUTF(producto.getArticulo());
                dos.writeDouble(producto.getPrecio());
                dos.writeInt(producto.getExistencias());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Leer los productos desde el fichero binario
        try (FileInputStream fis = new FileInputStream("productos.bin");
             DataInputStream dis = new DataInputStream(fis)) {

            while (dis.available() > 0) {
                String articulo = dis.readUTF();
                double precio = dis.readDouble();
                int existencias = dis.readInt();
                System.out.println("Artículo: " + articulo + ", Precio: " + precio + ", Existencias: " + existencias);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}