package subway.lines;

import java.util.List;

import subway.stations.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Integer distance;
    private List<StationResponse> stations;
    
    public LineResponse(Long id, String name, String color, int distance, List<StationResponse> stations)
    {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }
    
    public Long getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.name;
    }

    public String getColor()
    {
        return this.color;
    }

    public Integer getDistance()
    {
        return this.distance;
    }

    public List<StationResponse> getStations()
    {
        return this.stations;
    }
}
