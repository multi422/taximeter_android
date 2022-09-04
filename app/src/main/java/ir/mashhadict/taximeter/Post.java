package ir.mashhadict.taximeter;

import com.google.gson.annotations.SerializedName;

public class Post {

    private Integer id;
    private int idCode;
    private double lat;
    private double lng;
    private boolean psgZero, psgOne, psgTwo, psgThree;
    private String time;

    public Post(int idCode, double lat, double lng, boolean psgZero, boolean psgOne, boolean psgTwo, boolean psgThree) {
        this.idCode = idCode;
        this.lat = lat;
        this.lng = lng;
        this.psgZero = psgZero;
        this.psgOne = psgOne;
        this.psgTwo = psgTwo;
        this.psgThree = psgThree;
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

    public boolean isPsgZero() {
        return psgZero;
    }

    public boolean isPsgOne() {
        return psgOne;
    }

    public boolean isPsgTwo() {
        return psgTwo;
    }

    public boolean isPsgThree() {
        return psgThree;
    }

    public String getTime() {
        return time;
    }
}
