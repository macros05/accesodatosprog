import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.Scanner;

public class Principal {

	public static void guardarCancion(Canciones cancion) {
	    Transaction transaction = null;
	    Session session = HibernateUtil.getSessionFactory().openSession();  // Abrir la sesión aquí
	    try {
	        transaction = session.beginTransaction();
	        session.persist(cancion);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        session.close();  // Cerrar la sesión después de usarla
	    }
	}

    public static void buscarCancion(String nombreCancion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String busqueda = "FROM Canciones WHERE nombre = :nombre";
            Query<Canciones> query = session.createQuery(busqueda, Canciones.class);
            query.setParameter("nombre", nombreCancion);

            Canciones cancion = query.uniqueResult();
            if (cancion != null) {
                System.out.println(cancion.toString());
            } else {
                System.out.println("Canción no encontrada.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al buscar");
        }
    }

    public static void actualizarCancion(int codigo, String nuevoNombre, String nuevoCantante, String nuevoAlbum, LocalDate nuevaFechaLanzamiento) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Canciones cancion = session.get(Canciones.class, codigo);
            if (cancion != null) {
                cancion.setNombre(nuevoNombre);
                cancion.setCantante(nuevoCantante);
                cancion.setAlbum(nuevoAlbum);
                cancion.setFechaLanzamiento(nuevaFechaLanzamiento);

                session.update(cancion);
                transaction.commit();
                System.out.println("Canción actualizada correctamente.");
            } else {
                System.out.println("Canción no encontrada.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al actualizar la canción.");
        }
    }

    public static void borrarCancion(int codigo) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Canciones cancion = session.get(Canciones.class, codigo);
            if (cancion != null) {
                session.delete(cancion);
                transaction.commit();
                System.out.println("Canción eliminada correctamente.");
            } else {
                System.out.println("Canción no encontrada.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al eliminar la canción.");
        }
    }

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcion = 1;

         while (opcion != 0) {
      
            System.out.println("Introduzca :\n"
                    + "1. Para introducir una nueva cancion \n"
                    + "2. Para buscar una canción \n"
                    + "3. Para modificar detalles de una cancion \n"
                    + "4. Borrar una cancion \n"
                    + "0. Salir");
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
            
            
            case 1: 
                System.out.println("Introduzca el nombre de la canción");
                String nombreCancion = teclado.nextLine();

                System.out.println("Introduzca el nombre del cantante");
                String cantanteCancion = teclado.nextLine();

                System.out.println("Introduzca el albúm del cantante");
                String albumCancion = teclado.nextLine();

                System.out.print("Fecha de lanzamiento (YYYY-MM-DD): ");
                String fecha = teclado.nextLine();
                LocalDate fechaLanzamiento = LocalDate.parse(fecha);

                Canciones cancionIntroducida = new Canciones(nombreCancion, cantanteCancion, albumCancion, fechaLanzamiento);
                guardarCancion(cancionIntroducida);
            
                break;

            case 2:  
                System.out.println("Introduzca el nombre de la canción");
                nombreCancion = teclado.nextLine();
                buscarCancion(nombreCancion);
            
                break;
                
            case 3: 
                System.out.println("Introduzca el codigo de la canción");
                int codigoCancion = teclado.nextInt();
                teclado.nextLine();

                System.out.println("Introduzca el nuevo nombre de la canción");
                String nuevoNombre = teclado.nextLine();

                System.out.println("Introduzca el nuevo nombre del cantante");
                String nuevoCantante = teclado.nextLine();

                System.out.println("Introduzca el albúm del cantante");
                String nuevoAlbum = teclado.nextLine();

                System.out.print("Fecha de lanzamiento (YYYY-MM-DD): ");
                String nuevaFecha = teclado.nextLine();

                fechaLanzamiento = LocalDate.parse(nuevaFecha);
                actualizarCancion(codigoCancion, nuevoNombre, nuevoCantante, nuevoAlbum, fechaLanzamiento);
            
                break;
                
            case 4: 
                System.out.println("Introduzca el codigo de la cancion a borrar");
                codigoCancion = teclado.nextInt();
                teclado.nextLine();
                borrarCancion(codigoCancion);
            break;
            
            case 0: 
            	System.out.println("Saliendo del programa");
            	
            	break;
            	
            default: 
            	System.out.println("Opcion no valida");
            	break;
            } 	
        }

        teclado.close();
    }
}
