package Model;

public class Event {
    private final String date;
    private final String time;
    private final String place;
    private final String station;
    private final Float tamax;
    private final Float tamin;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public String getStation() {
        return station;
    }

    public Float getTamax() {
        return tamax;
    }

    public Float getTamin() {
        return tamin;
    }

    public Event(String date, String time, String place, String station, Float tamax, Float tamin) {
        this.date = date;
        this.time = time;
        this.place = place;
        this.station = station;
        this.tamax = tamax;
        this.tamin = tamin;
    }
}
