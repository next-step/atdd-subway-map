package subway;

import java.util.ArrayList;
import java.util.List;

public class SubwayLineRequest {
    private String name;
    private String color;

    private List<String> stationNames = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<String> getStationNames() {
        return stationNames;
    }
}
