package map.net.eletricago.classes;

public class ResultResponse {

    private String ZonaName;
    private String Confidence;

    public ResultResponse() {
    }

    public String getZonaName() {
        return ZonaName;
    }

    public void setZonaName(String zonaName) {
        ZonaName = zonaName;
    }

    public String getConfidence() {
        return Confidence;
    }

    public void setConfidence(String confidence) {
        Confidence = confidence;
    }
}
