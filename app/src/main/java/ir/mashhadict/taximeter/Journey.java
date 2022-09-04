package ir.mashhadict.taximeter;

public class Journey {

    private Integer id;
    private int idDriver;
    private String startDate;
    private String startTime;
    private String finishDate;
    private String finishTime;
    private double srcLat;
    private double srcLng;
    private double desLat;
    private double desLng;
    private double distance;
    private double cost;
    private int psgNum;

    public Journey(int idDriver, String startDate, String startTime, String finishDate, String finishTime, double srcLat, double srcLng, double desLat, double desLng, double distance, double cost, int psgNum) {
        this.idDriver = idDriver;
        this.startDate = startDate;
        this.startTime = startTime;
        this.finishDate = finishDate;
        this.finishTime = finishTime;
        this.srcLat = srcLat;
        this.srcLng = srcLng;
        this.desLat = desLat;
        this.desLng = desLng;
        this.distance = distance;
        this.cost = cost;
        this.psgNum = psgNum;
    }

    public Integer getId() {
        return id;
    }

    public int getIdDriver() {
        return idDriver;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public double getSrcLat() {
        return srcLat;
    }

    public double getSrcLng() {
        return srcLng;
    }

    public double getDesLat() {
        return desLat;
    }

    public double getDesLng() {
        return desLng;
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
}