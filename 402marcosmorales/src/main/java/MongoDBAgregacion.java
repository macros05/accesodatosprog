import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.*;

import org.bson.Document;

import java.util.Arrays;
import java.util.Scanner;

public class MongoDBAgregacion {
    public static void main(String[] args) {
        // Conexión a MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://usuario_facturas:1234@143.47.47.116:27017/facturas");
        MongoDatabase database = mongoClient.getDatabase("facturas");
        MongoCollection<Document> collection = database.getCollection("facturas");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Menú de Operaciones =====");
            System.out.println("1. Cuántas facturas tiene un cliente");
            System.out.println("2. El importe total de una factura");
            System.out.println("3. El importe total de ventas de un cliente");
            System.out.println("4. Los productos más vendidos");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    contarFacturasCliente(collection);
                    break;
                case 2:
                    calcularImporteFactura(collection);
                    break;
                case 3:
                    calcularVentasCliente(collection);
                    break;
                case 4:
                    productosMasVendidos(collection);
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    mongoClient.close();
                    return;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }
        }
    }

    // Opción 1: Cuántas facturas tiene un cliente
    private static void contarFacturasCliente(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();

        AggregateIterable<Document> resultado = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("cliente", nombreCliente)), // Filtrar por cliente
                Aggregates.group("$cliente", Accumulators.sum("totalFacturas", 1)) // Contar facturas
        ));

        for (Document doc : resultado) {
            System.out.println("El cliente " + doc.getString("_id") + " tiene " + doc.getInteger("totalFacturas") + " facturas.");
        }
    }

    // Opción 2: El importe total de una factura
    private static void calcularImporteFactura(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el número de la factura: ");
        String numeroFactura = scanner.nextLine();

        // Agregación para calcular el importe total de una factura
        AggregateIterable<Document> resultado = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("numero_factura", numeroFactura)), // Filtrar por número de factura
                Aggregates.unwind("$lineas_factura"), // Descomponer líneas de factura
                Aggregates.group("$numero_factura", // Agrupar por número de factura
                        Accumulators.sum("importeTotal", new Document("$multiply", Arrays.asList(
                                "$lineas_factura.precio_unitario",
                                "$lineas_factura.cantidad"
                        )))
                )
        ));

        // Procesar el resultado
        boolean encontrado = false;
        for (Document doc : resultado) {
            encontrado = true;
            Object importeTotal = doc.get("importeTotal");

            // Manejo de tipos: Integer o Double
            double total = 0.0;
            if (importeTotal instanceof Integer) {
                total = ((Integer) importeTotal).doubleValue();
            } else if (importeTotal instanceof Double) {
                total = (Double) importeTotal;
            }

            System.out.println("El importe total de la factura " + numeroFactura + " es: " + total);
        }

        if (!encontrado) {
            System.out.println("No se encontró la factura con el número: " + numeroFactura);
        }
    }


    // Opción 3: El importe total de ventas de un cliente
    private static void calcularVentasCliente(MongoCollection<Document> collection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();

        // Agregación para calcular el importe total de ventas de un cliente
        AggregateIterable<Document> resultado = collection.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("cliente", nombreCliente)), // Filtrar por cliente
                Aggregates.unwind("$lineas_factura"), // Descomponer líneas de factura
                Aggregates.group("$cliente", // Agrupar por cliente
                        Accumulators.sum("totalVentas", new Document("$multiply", Arrays.asList(
                                "$lineas_factura.precio_unitario",
                                "$lineas_factura.cantidad"
                        )))
                )
        ));

        // Procesar el resultado
        boolean encontrado = false;
        for (Document doc : resultado) {
            encontrado = true;
            Object totalVentas = doc.get("totalVentas");

            // Manejo dinámico de tipos: Integer o Double
            double total = 0.0;
            if (totalVentas instanceof Integer) {
                total = ((Integer) totalVentas).doubleValue();
            } else if (totalVentas instanceof Double) {
                total = (Double) totalVentas;
            }

            System.out.println("El total de ventas para " + nombreCliente + " es: " + total);
        }

        if (!encontrado) {
            System.out.println("No se encontraron ventas para el cliente: " + nombreCliente);
        }
    }

    // Opción 4: Los productos más vendidos
    private static void productosMasVendidos(MongoCollection<Document> collection) {
        AggregateIterable<Document> resultado = collection.aggregate(Arrays.asList(
                Aggregates.unwind("$lineas_factura"), // Descomponer líneas de factura
                Aggregates.group("$lineas_factura.articulo", // Agrupar por artículo
                        Accumulators.sum("totalCantidad", "$lineas_factura.cantidad")), // Sumar cantidades vendidas
                Aggregates.sort(Sorts.descending("totalCantidad")) // Ordenar por cantidad vendida descendente
        ));

        System.out.println("Productos más vendidos:");
        for (Document doc : resultado) {
            System.out.println("Producto: " + doc.getString("_id") + " - Cantidad vendida: " + doc.getInteger("totalCantidad"));
        }
    }
}
