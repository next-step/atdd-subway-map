package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationSection;
import org.springframework.stereotype.Component;

@Component
public class StationLineMapper {

    public StationLine of(StationLineRequest request) {

        StationLine stationLine = new StationLine(
                request.getName(),
                request.getColor());
        StationSection stationSection = new StationSection(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
        stationLine.addSection(stationSection);
        return stationLine;
    }

    public StationLineResponse of(StationLine savedStationLine, Station upStation,
                                  Station downStation) {

        List<StationResponse> stationResponses = List.of(
                new StationResponse(upStation.getId(), upStation.getName()),
                new StationResponse(downStation.getId(), downStation.getName()));
        return new StationLineResponse(
                savedStationLine.getId(),
                savedStationLine.getName(),
                savedStationLine.getColor(), stationResponses
        );
    }

    public StationLineResponse of(StationLine savedStationLine, List<Station> lineStations) {
        List<StationResponse> stationResponses = lineStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new StationLineResponse(
                savedStationLine.getId(),
                savedStationLine.getName(),
                savedStationLine.getColor(), stationResponses
        );
    }
}