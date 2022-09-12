package ir.mashhadict.taximeter;

public class LivLoc {

    private Integer id;
    private int idCode;
    private double lat;
    private double lng;
    private double time;

    public LivLoc(int idCode, double lat, double lng) {
        this.idCode = idCode;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getId() {
        return id;
    }

    public int getIdCode() {
        return idCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getTime() {
        return time;
    }
}
