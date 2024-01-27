package subway;

import java.util.ArrayList;
import java.util.List;

public class LinesResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public String getColor() {
        return color;
    }
}
