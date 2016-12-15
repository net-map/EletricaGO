package map.net.eletricago.classes;

import java.io.Serializable;

public class AccessPoint implements Serializable {
    private String BSSID;
    private Double RSSI;

    public AccessPoint(String BSSID, Double RSSI) {
        this.BSSID = BSSID;
        this.RSSI = RSSI;
    }


    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }


    public Double getRSSI() {
        return RSSI;
    }

    public void setRSSI(Double RSSI) {
        this.RSSI = RSSI;
    }
}
