import mysql.connector
from mysql.connector import Error
import datetime

# Datos de conexión remota (Oracle MySQL remoto)
HOST = '143.47.56.119'
DATABASE = 'gestion_cursos'
USER = 'accesodatos'
PASSWORD = 'accesodatos123'
PORT = '3306'

# Conexión a la base de datos
def conectar():
    try:
        conexion = mysql.connector.connect(
            host=HOST,
            database=DATABASE,
            user=USER,
            password=PASSWORD,
            port=PORT
        )
        if conexion.is_connected():
            print("Conexión exitosa a la base de datos")
            return conexion
    except Error as e:
        print("Error al conectar a la base de datos:", e)
        return None

# 1. Matricular a un estudiante en un curso
def matricular_estudiante(id_estudiante, id_curso, fecha_inicio, fecha_fin):
    conexion = conectar()
    if conexion:
        try:
            cursor = conexion.cursor()
            fecha_actual = datetime.date.today()
            query = """INSERT INTO Matriculas (id_estudiante, id_curso, fecha_matricula, fecha_inicio, fecha_fin) 
                       VALUES (%s, %s, %s, %s, %s)"""
            valores = (id_estudiante, id_curso, fecha_actual, fecha_inicio, fecha_fin)
            cursor.execute(query, valores)
            conexion.commit()
            print("Estudiante matriculado correctamente.")
        except Error as e:
            print("Error al matricular estudiante:", e)
        finally:
            cursor.close()
            conexion.close()

# 2. Listado de estudiantes en un curso
def listar_estudiantes_curso(id_curso):
    conexion = conectar()
    if conexion:
        try:
            cursor = conexion.cursor()
            query = """SELECT E.id_estudiante, E.nombre, E.correo 
                       FROM Estudiantes E 
                       JOIN Matriculas M ON E.id_estudiante = M.id_estudiante 
                       WHERE M.id_curso = %s"""
            cursor.execute(query, (id_curso,))
            estudiantes = cursor.fetchall()
            print("Listado de estudiantes en el curso:")
            for estudiante in estudiantes:
                print(estudiante)
        except Error as e:
            print("Error al listar estudiantes:", e)
        finally:
            cursor.close()
            conexion.close()

# 3. Baja de un estudiante en un curso
def dar_baja_estudiante(id_estudiante, id_curso):
    conexion = conectar()
    if conexion:
        try:
            cursor = conexion.cursor()
            query = """DELETE FROM Matriculas 
                       WHERE id_estudiante = %s AND id_curso = %s"""
            cursor.execute(query, (id_estudiante, id_curso))
            conexion.commit()
            print("Estudiante dado de baja del curso correctamente.")
        except Error as e:
            print("Error al dar de baja al estudiante:", e)
        finally:
            cursor.close()
            conexion.close()

# 4. Actualizar el precio de un curso
def actualizar_precio_curso(id_curso, nuevo_precio):
    conexion = conectar()
    if conexion:
        try:
            cursor = conexion.cursor()
            query = "UPDATE Cursos SET precio = %s WHERE id_curso = %s"
            cursor.execute(query, (nuevo_precio, id_curso))
            conexion.commit()
            print("Precio del curso actualizado correctamente.")
        except Error as e:
            print("Error al actualizar el precio del curso:", e)
        finally:
            cursor.close()
            conexion.close()

# 5. Emitir recibos por estudiante
def emitir_recibo(id_estudiante):
    conexion = conectar()
    if conexion:
        try:
            cursor = conexion.cursor()
            query = """SELECT E.nombre, C.nombre AS curso, C.precio, M.fecha_matricula
                       FROM Estudiantes E
                       JOIN Matriculas M ON E.id_estudiante = M.id_estudiante
                       JOIN Cursos C ON M.id_curso = C.id_curso
                       WHERE E.id_estudiante = %s"""
            cursor.execute(query, (id_estudiante,))
            recibos = cursor.fetchall()
            print("Recibo del estudiante:")
            for recibo in recibos:
                nombre, curso, precio, fecha = recibo
                print(f"Estudiante: {nombre} | Curso: {curso} | Precio: {precio}€ | Fecha de matriculación: {fecha}")
        except Error as e:
            print("Error al emitir recibo:", e)
        finally:
            cursor.close()
            conexion.close()

# Pruebas del programa
if __name__ == "__main__":
    # Ejemplo de pruebas
    print("1. Matricular a un estudiante")
    matricular_estudiante(1, 2, "2024-07-01", "2024-12-01")

    print("\n2. Listar estudiantes en un curso")
    listar_estudiantes_curso(2)

    print("\n3. Dar de baja a un estudiante")
    dar_baja_estudiante(1, 2)

    print("\n4. Actualizar precio de un curso")
    actualizar_precio_curso(2, 150.0)

    print("\n5. Emitir recibo de un estudiante")
    emitir_recibo(1)
