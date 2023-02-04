package subway;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.section.Section;
import subway.section.SectionCreateRequest;
import subway.station.Station;
import subway.station.StationResponse;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public Line of(LineCreateRequest lineCreateRequest, Station upStation, Station downStation) {
        return new Line(lineCreateRequest.getName(), lineCreateRequest.getColor(), upStation, downStation, lineCreateRequest.getDistance());
    }

    public LineResponse of(Line line) {
        List<StationResponse> stationResponseList = line.getStations().stream().map(this::createStationResponse).collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), stationResponseList);
    }

    public StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

}
