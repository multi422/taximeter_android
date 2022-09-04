package ir.mashhadict.taximeter;

public class Rate {

    private Integer id;
    private int events;
    private int weather;
    private int district;

    public Rate(int events, int weather, int district) {
        this.events = events;
        this.weather = weather;
        this.district = district;
    }

    public Integer getId() {
        return id;
    }

    public int getEvents() {
        return events;
    }

    public int getWeather() {
        return weather;
    }

    public int getDistrict() {
        return district;
    }
}
