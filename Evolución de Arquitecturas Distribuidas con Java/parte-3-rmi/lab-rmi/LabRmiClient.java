import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LabRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23001);
        LabInventoryService service =
                (LabInventoryService) registry.lookup("labInventoryService");

        System.out.println("Equipos:");
        for (String item : service.consultarEquipos()) {
            System.out.println("- " + item);
        }

        System.out.println("Consulta OSC-01: " + service.consultarEquipo("OSC-01"));
        System.out.println("Reservar OSC-01: " + service.reservarEquipo("OSC-01"));
        System.out.println("Consulta OSC-01: " + service.consultarEquipo("OSC-01"));
        System.out.println("Liberar OSC-01: " + service.liberarEquipo("OSC-01"));
    }
}
