package moralesmarcos105;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creamos algunos objetos Producto y los agregamos a una lista
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Manzana", 0.75, 100));
        productos.add(new Producto("Pera", 1.20, 50));
        productos.add(new Producto("Naranja", 0.90, 75));

        // Guardamos la lista de productos en un archivo binario
        guardarProductosEnArchivo(productos, "productos.dat");

        // Leemos la lista de productos desde el archivo binario
        List<Producto> productosLeidos = leerProductosDesdeArchivo("productos.dat");

        // Mostramos los productos leídos
        if (productosLeidos != null) {
            for (Producto producto : productosLeidos) {
                System.out.println(producto);
            }
        }
    }

    // Método para serializar (guardar) una lista de productos en un archivo binario
    public static void guardarProductosEnArchivo(List<Producto> productos, String archivo) {
        try (FileOutputStream fos = new FileOutputStream(archivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // Serializamos toda la lista de productos de una sola vez
            oos.writeObject(productos);
            System.out.println("Lista de productos guardada correctamente en " + archivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para deserializar (leer) una lista de productos desde un archivo binario
    public static List<Producto> leerProductosDesdeArchivo(String archivo) {
        List<Producto> productos = null;

        try (FileInputStream fis = new FileInputStream(archivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Deserializamos toda la lista de productos de una sola vez
            productos = (List<Producto>) ois.readObject();
            System.out.println("Lista de productos leída correctamente desde " + archivo);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return productos;
    }
}
