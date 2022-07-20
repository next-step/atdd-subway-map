package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations.stream()
                                .map(station -> new StationResponse(station.getId(), station.getName()))
                                .collect(Collectors.toList());
    }
}
