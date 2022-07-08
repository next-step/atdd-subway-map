package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

import java.util.Arrays;
import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public LineResponse(Line line, StationRepository stationRepository) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        stations = stationRepository.findAllById(Arrays.asList(line.getUpStationId(), line.getDownStationId()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }
}
