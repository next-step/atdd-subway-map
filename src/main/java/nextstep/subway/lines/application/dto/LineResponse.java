package nextstep.subway.lines.application.dto;

import nextstep.subway.lines.domain.Line;
import nextstep.subway.stations.applicaion.dto.StationResponse;
import nextstep.subway.stations.domain.Station;

import java.util.Arrays;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

    public LineResponse() {

    }
    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
//        this.stations = Arrays.asList(new StationResponse(line.getUpStation()), new StationResponse(line.getDownStation()));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public List<StationResponse> getStations() {
        return this.stations;
    }

}
