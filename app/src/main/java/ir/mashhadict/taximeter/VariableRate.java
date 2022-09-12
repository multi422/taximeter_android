package ir.mashhadict.taximeter;

public class VariableRate {

    private Integer id;
    private boolean time;
    private boolean weather;

    public VariableRate(boolean time, boolean weather) {
        this.time = time;
        this.weather = weather;
    }


    public Integer getId() {
        return id;
    }

    public boolean isTime() {
        return time;
    }

    public boolean isWeather() {
        return weather;
    }
}
