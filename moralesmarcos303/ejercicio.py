from peewee import *
import mysql.connector

# Conexión a la base de datos
db = MySQLDatabase(
    'inventario', 
    user='usuario_java', 
    password='java123', 
    host='localhost', 
    port=3306
)

# Modelos
class BaseModel(Model):
    class Meta:
        database = db

class Ubicacion(BaseModel):
    cod = AutoField()
    lugar = CharField(max_length=255)

class Maquina(BaseModel):
    codigo = AutoField()
    modelo = CharField(max_length=255)
    descripcion = TextField(null=True)
    ram = CharField(max_length=50, null=True)
    hd = CharField(max_length=50, null=True)
    cpu = CharField(max_length=50, null=True)
    cod_ubicacion = ForeignKeyField(Ubicacion, backref='maquinas', null=True, on_delete='SET NULL')


def inicializar_bd():
    db.connect()
    db.create_tables([Ubicacion, Maquina])
    print("Tablas creadas correctamente.")
    db.close()


def alta_maquina(modelo, descripcion, ram, hd, cpu, cod_ubicacion):
    maquina = Maquina.create(
        modelo=modelo,
        descripcion=descripcion,
        ram=ram,
        hd=hd,
        cpu=cpu,
        cod_ubicacion=cod_ubicacion
    )
    print(f"Máquina registrada: {maquina.modelo}")


def consultar_maquina_por_modelo(modelo):
    maquinas = Maquina.select().where(Maquina.modelo == modelo)
    for maquina in maquinas:
        print(f"ID: {maquina.codigo}, Modelo: {maquina.modelo}, RAM: {maquina.ram}, HD: {maquina.hd}, CPU: {maquina.cpu}, Ubicación: {maquina.cod_ubicacion.lugar if maquina.cod_ubicacion else 'Sin ubicación'}")


def actualizar_maquina(codigo, ram=None, hd=None, cod_ubicacion=None):
    query = Maquina.update(
        ram=ram if ram else Maquina.ram,
        hd=hd if hd else Maquina.hd,
        cod_ubicacion=cod_ubicacion if cod_ubicacion else Maquina.cod_ubicacion
    ).where(Maquina.codigo == codigo)
    query.execute()
    print(f"Máquina {codigo} actualizada.")


def listar_maquinas_por_ubicacion():
    ubicaciones = Ubicacion.select()
    for ubicacion in ubicaciones:
        print(f"Ubicación: {ubicacion.lugar}")
        maquinas = ubicacion.maquinas
        for maquina in maquinas:
            print(f"  - Modelo: {maquina.modelo}, RAM: {maquina.ram}, HD: {maquina.hd}, CPU: {maquina.cpu}")


if __name__ == "__main__":
    inicializar_bd()

    # Crear ubicaciones
    Ubicacion.create(lugar="Almacén 1")
    Ubicacion.create(lugar="Almacén 2")

    # Alta de máquinas
    alta_maquina("Dell XPS", "Laptop potente", "16GB", "512GB", "Intel i7", 1)
    alta_maquina("MacBook Air", "Ultraligera", "8GB", "256GB", "M1", 2)

    # Consultar máquinas
    consultar_maquina_por_modelo("Dell XPS")

    # Actualizar máquina
    actualizar_maquina(1, ram="32GB", cod_ubicacion=2)

    # Listar máquinas por ubicación
    listar_maquinas_por_ubicacion()
