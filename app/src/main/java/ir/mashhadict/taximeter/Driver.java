package ir.mashhadict.taximeter;

public class Driver {

    private Integer id;
    private int identityCode;
    private int password;
    private String firstName;
    private String lastName;
    private int zone;
    private int type;
    private int vehicle;
    private String carModel;
    private String pelak;
    private String color;
    private boolean isConfirmed;

    public Driver(int identityCode, int password, String firstName, String lastName, int zone, int type, int vehicle, String carModel, String pelak, String color) {
        this.identityCode = identityCode;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zone = zone;
        this.type = type;
        this.vehicle = vehicle;
        this.carModel = carModel;
        this.pelak = pelak;
        this.color = color;
    }


    public Integer getId() {
        return id;
    }

    public int getIdentityCode() {
        return identityCode;
    }

    public int getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public String getCarModel() {
        return carModel;
    }

    public String getPelak() {
        return pelak;
    }

    public String getColor() {
        return color;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

}
