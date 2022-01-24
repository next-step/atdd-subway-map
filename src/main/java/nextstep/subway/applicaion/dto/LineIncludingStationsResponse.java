package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineIncludingStationsResponse extends LineResponse {

    private List<StationResponse> stations;

    public LineIncludingStationsResponse(Line line) {
        super(line);
        this.stations = line.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
