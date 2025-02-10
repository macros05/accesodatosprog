import socket
import random

# Frases para el servicio de "Cita del dia"
quotes = [
    "La vida es lo que pasa mientras estas ocupado haciendo otros planes. - John Lennon",
    "El exito es la suma de pequeños esfuerzos repetidos dia tras dia. - Robert Collier",
    "No cuentes los dias, haz que los dias cuenten. - Muhammad Ali",
    "Cree que puedes y ya estas a medio camino. - Theodore Roosevelt",
    "La mejor manera de predecir el futuro es creandolo. - Peter Drucker"
]

# 1. Crear el socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# 2. Enlazar el socket a una dirección y puerto
server_address = ('192.168.56.1', 2017)  # Acepta conexiones desde cualquier IP
server_socket.bind(server_address)

# 3. Poner el socket en modo escucha
server_socket.listen(5)  # Permite hasta 5 conexiones

print(f"Servidor de 'Cita del dia' escuchando en {server_address}")

while True:
    # 4. Aceptar conexiones
    client_socket, client_address = server_socket.accept()
    print(f"Conexión aceptada de {client_address}")

    # 5. Enviar una cita aleatoria
    quote = random.choice(quotes)
    client_socket.sendall(quote.encode('utf-8'))  # Enviar la cita

    # 6. Cerrar la conexión
    client_socket.close()
