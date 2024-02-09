package subway.station;

public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
