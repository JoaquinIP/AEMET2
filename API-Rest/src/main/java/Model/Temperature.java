package Model;

public class Temperature {
    private final String place;
    private final String date;
    private final float value;

    public Temperature(String place, String date, float value) {
        this.place = place;
        this.date = date;
        this.value = value;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public float getValue() {
        return value;
    }
}