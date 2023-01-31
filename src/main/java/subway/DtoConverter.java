package subway;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.subway.Station;
import subway.subway.StationResponse;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public Line of(LineCreateRequest lineCreateRequest, Station upStation, Station downStation) {
        return new Line(lineCreateRequest.getName(), lineCreateRequest.getColor(), upStation, downStation, lineCreateRequest.getDistance());
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
