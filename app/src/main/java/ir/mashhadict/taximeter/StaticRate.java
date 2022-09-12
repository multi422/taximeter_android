package ir.mashhadict.taximeter;

public class StaticRate {

    private Integer id;
    private int zone;
    private int type;
    private int vehicle;
    private int initional;
    private int distance;
    private int time;


    public StaticRate(int zone, int type, int vehicle, int initional, int distance, int time) {
        this.zone = zone;
        this.type = type;
        this.vehicle = vehicle;
        this.initional = initional;
        this.distance = distance;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public int getZone() {
        return zone;
    }

    public int getType() {
        return type;
    }

    public int getVehicle() {
        return vehicle;
    }

    public int getInitional() {
        return initional;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }
}
