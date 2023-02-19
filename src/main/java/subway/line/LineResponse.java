package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;

    private String name;
    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    private List<StationResponse> stations;

    public LineResponse(){}

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {return id;}

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public List<StationResponse> getStations(){ return stations; }

    public static LineResponse createLineResponse(Line line) {
        List<Section> sections = line.getSections();
        List<StationResponse> stations = new ArrayList<>();

        for(int i=0; i<sections.size(); i++){
            if(i==0){
                Station upStation = sections.get(i).getUpStation();
                StationResponse stationResponse = new StationResponse(upStation.getId(), upStation.getName());
                stations.add(stationResponse);
            }

            Station downStation = sections.get(i).getDownStation();
            StationResponse stationResponse = new StationResponse(downStation.getId(), downStation.getName());
            stations.add(stationResponse);
        }

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations
        );
    }
}
