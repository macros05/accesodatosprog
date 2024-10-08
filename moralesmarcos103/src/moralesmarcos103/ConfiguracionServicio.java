package moralesmarcos103;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ConfiguracionServicio {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Properties configuracion = new Properties();

        System.out.print("Introduce el nombre del fichero de configuración: ");
        String nombreFichero = scanner.nextLine();

        try {
            configuracion.load(new FileInputStream(nombreFichero));

            String propiedad;
            do {
                System.out.print("Introduce el nombre de la propiedad que quieres consultar (o 'salir' para terminar): ");
                propiedad = scanner.nextLine();

                if (!propiedad.equalsIgnoreCase("salir")) {
                    String valor = configuracion.getProperty(propiedad);

                    if (valor != null) {
                        System.out.println("El valor de la propiedad '" + propiedad + "' es: " + valor);
                    } else {
                        System.out.println("La propiedad '" + propiedad + "' no existe.");
                    }
                }
            } while (!propiedad.equalsIgnoreCase("salir"));

        } catch (FileNotFoundException fnfe) {
            System.out.println("El fichero no existe: " + nombreFichero);
        } catch (IOException ioe) {
            System.out.println("Error al leer el fichero: " + ioe.getMessage());
        } finally {
            scanner.close();
        }
    }
}
