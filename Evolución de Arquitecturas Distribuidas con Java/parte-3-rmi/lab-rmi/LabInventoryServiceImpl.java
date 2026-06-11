import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LabInventoryServiceImpl extends UnicastRemoteObject implements LabInventoryService {
    private final Map<String, LabEquipment> equipment = new LinkedHashMap<>();

    public LabInventoryServiceImpl() throws RemoteException {
        equipment.put("OSC-01", new LabEquipment("OSC-01", "Osciloscopio", "Laboratorio Electronica", false));
        equipment.put("ARD-01", new LabEquipment("ARD-01", "Kit Arduino", "Laboratorio Software", false));
        equipment.put("RPI-01", new LabEquipment("RPI-01", "Raspberry Pi", "Laboratorio Redes", false));
    }

    @Override
    public synchronized List<String> consultarEquipos() throws RemoteException {
        List<String> result = new ArrayList<>();
        for (LabEquipment item : equipment.values()) {
            result.add(item.toString());
        }
        return result;
    }

    @Override
    public synchronized String consultarEquipo(String codigo) throws RemoteException {
        LabEquipment item = equipment.get(normalize(codigo));
        if (item == null) {
            return "ERROR_EQUIPO_NO_EXISTE";
        }
        return item.toString();
    }

    @Override
    public synchronized boolean reservarEquipo(String codigo) throws RemoteException {
        LabEquipment item = equipment.get(normalize(codigo));
        if (item == null || item.isReserved()) {
            return false;
        }
        item.reserve();
        return true;
    }

    @Override
    public synchronized boolean liberarEquipo(String codigo) throws RemoteException {
        LabEquipment item = equipment.get(normalize(codigo));
        if (item == null || !item.isReserved()) {
            return false;
        }
        item.release();
        return true;
    }

    private String normalize(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }
}
