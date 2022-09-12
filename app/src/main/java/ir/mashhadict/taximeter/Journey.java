package ir.mashhadict.taximeter;

public class Journey {

    private Integer id;
    private int idDriver;
    private double lat;
    private double lng;
    private double distance;
    private double cost;
    private int psgNum;
    private boolean isStart = false;
    private boolean isFinish = false;
    private boolean isMale = true;
    private boolean payMet = false;
    private String time;

    public Journey(int idDriver, double lat, double lng, double distance, double cost, int psgNum, boolean isStart, boolean isFinish, boolean isMale, boolean payMet) {
        this.idDriver = idDriver;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.cost = cost;
        this.psgNum = psgNum;
        this.isStart = isStart;
        this.isFinish = isFinish;
        this.isMale = isMale;
        this.payMet = payMet;
    }

    public Integer getId() {
        return id;
    }

    public int getIdDriver() {
        return idDriver;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getDistance() {
        return distance;
    }

    public double getCost() {
        return cost;
    }

    public int getPsgNum() {
        return psgNum;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean isMale() {
        return isMale;
    }

    public boolean isPayMet() {
        return payMet;
    }

    public String getTime() {
        return time;
    }
}