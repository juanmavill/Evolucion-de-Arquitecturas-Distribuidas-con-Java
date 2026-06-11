import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RoomClient {
    public static void main(String[] args) throws Exception {
        String operation;
        String roomId;

        if (args.length == 2) {
            operation = args[0];
            roomId = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Operacion (CONSULTAR_SALON, RESERVAR_SALON, LIBERAR_SALON): ");
            operation = scanner.nextLine();
            System.out.print("Salon (E301, E302, E303, E304): ");
            roomId = scanner.nextLine();
        }

        Socket socket = new Socket("127.0.0.1", 35001);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        out.println(operation + "," + roomId);
        System.out.println("Respuesta del servidor: " + in.readLine());

        in.close();
        out.close();
        socket.close();
    }
}
