import socket

SERVER_IP = "143.47.47.116"  # IP del servidor
PORT = 5000

try:
    nombre_usuario = input("Introduce tu nombre: " + "\n").strip()
    mensaje = f"SOY {nombre_usuario}"

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as cliente:
        cliente.settimeout(5)  # Si no recibe respuesta en 5 segundos, se detiene
        print(f"Conectando a {SERVER_IP}:{PORT}...")
        cliente.connect((SERVER_IP, PORT))
        print("Conexión establecida. Enviando mensaje...")

        cliente.sendall(mensaje.encode())

        try:
            respuesta = cliente.recv(1024).decode().strip()
            if respuesta:
                print(f"✅ Respuesta del servidor: {respuesta}")
            else:
                print("⚠ El servidor no envió ninguna respuesta.")
        except socket.timeout:
            print("⚠ Error: El servidor no respondió a tiempo.")

except ConnectionResetError:
    print("❌ Error: El servidor cerró la conexión de forma inesperada.")
except ConnectionRefusedError:
    print("❌ Error: No se pudo conectar al servidor. Verifica que esté ejecutándose.")
except Exception as e:
    print(f"⚠ Error inesperado: {e}")
