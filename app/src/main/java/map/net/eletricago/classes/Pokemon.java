package map.net.eletricago.classes;

public class Pokemon {
    String name;
    String file_name;
    String zone;
    Boolean captured;

    public Pokemon() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Boolean getCaptured() {
        return captured;
    }

    public void setCaptured(Boolean captured) {
        this.captured = captured;
    }
}
