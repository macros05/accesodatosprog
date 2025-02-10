import socket

# Configuración del servidor
server_address = ('143.47.47.116', 5000)

# 1. Crear el socket del cliente
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    # 2. Conectar al servidor
    client_socket.connect(server_address)
    print(f"Conectado al servidor {server_address}")

    # 3. Recibir la pregunta del servidor
    question = client_socket.recv(1024).decode('utf-8')
    print(f"Servidor: {question}")

    # 4. Enviar el nombre en UTF-8
    name = input("Ingresa tu nombre: ")
    client_socket.sendall((name + '\n').encode('utf-8'))
    # 5. Recibir el saludo personalizado
    response = client_socket.recv(1024).decode('utf-8')
    print(f"Servidor: {response}")

finally:
    # 6. Cerrar la conexión
    client_socket.close()