package subway;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.subway.Station;
import subway.subway.StationResponse;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public Line of(LineRequest lineRequest, Station upStation, Station downStation) {
        return new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
    }

    public LineResponse of(Line line) {
        List<StationResponse> stationResponseList = line.getStationList().stream().map(this::createStationResponse).collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), stationResponseList);
    }

    public StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
