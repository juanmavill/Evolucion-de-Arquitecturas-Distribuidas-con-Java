import java.util.LinkedHashMap;
import java.util.Map;

public class RoomRepository {
    private final Map<String, Boolean> rooms = new LinkedHashMap<>();

    public RoomRepository() {
        rooms.put("E301", false);
        rooms.put("E302", false);
        rooms.put("E303", false);
        rooms.put("E304", false);
    }

    public synchronized String consult(String roomId) {
        if (!rooms.containsKey(roomId)) {
            return "ERROR_SALON_NO_EXISTE";
        }
        return rooms.get(roomId) ? "SALON_RESERVADO" : "SALON_DISPONIBLE";
    }

    public synchronized String reserve(String roomId) {
        if (!rooms.containsKey(roomId)) {
            return "ERROR_SALON_NO_EXISTE";
        }
        if (rooms.get(roomId)) {
            return "SALON_RESERVADO";
        }
        rooms.put(roomId, true);
        return "RESERVA_EXITOSA";
    }

    public synchronized String release(String roomId) {
        if (!rooms.containsKey(roomId)) {
            return "ERROR_SALON_NO_EXISTE";
        }
        if (!rooms.get(roomId)) {
            return "SALON_DISPONIBLE";
        }
        rooms.put(roomId, false);
        return "LIBERACION_EXITOSA";
    }
}
