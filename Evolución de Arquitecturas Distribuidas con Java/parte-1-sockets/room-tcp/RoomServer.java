import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RoomServer {
    public static void main(String[] args) throws Exception {
        RoomRepository repository = new RoomRepository();
        ServerSocket serverSocket = new ServerSocket(35001);
        System.out.println("RoomServer TCP escuchando en puerto 35001...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            String response = processRequest(request, repository);
            out.println(response);

            in.close();
            out.close();
            clientSocket.close();
        }
    }

    private static String processRequest(String request, RoomRepository repository) {
        if (request == null) {
            return "ERROR_OPERACION_INVALIDA";
        }

        String[] parts = request.split(",");
        if (parts.length != 2) {
            return "ERROR_OPERACION_INVALIDA";
        }

        String operation = parts[0].trim();
        String roomId = parts[1].trim().toUpperCase();

        switch (operation) {
            case "CONSULTAR_SALON":
                return repository.consult(roomId);
            case "RESERVAR_SALON":
                return repository.reserve(roomId);
            case "LIBERAR_SALON":
                return repository.release(roomId);
            default:
                return "ERROR_OPERACION_INVALIDA";
        }
    }
}
