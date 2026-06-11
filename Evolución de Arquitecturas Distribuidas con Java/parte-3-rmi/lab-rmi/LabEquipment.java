import java.io.Serializable;

public class LabEquipment implements Serializable {
    private String code;
    private String name;
    private String laboratory;
    private boolean reserved;

    public LabEquipment(String code, String name, String laboratory, boolean reserved) {
        this.code = code;
        this.name = name;
        this.laboratory = laboratory;
        this.reserved = reserved;
    }

    public String getCode() {
        return code;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void reserve() {
        reserved = true;
    }

    public void release() {
        reserved = false;
    }

    @Override
    public String toString() {
        String status = reserved ? "reservado" : "disponible";
        return code + " - " + name + " - " + laboratory + " - " + status;
    }
}
