package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.Stations;
import org.springframework.stereotype.Component;

@Component
public class StationLineMapper {

    public StationLine of(StationLineRequest request,
                          Station upStation,
                          Station downStation) {

        return new StationLine(
                request.getName(),
                request.getColor(),
                request.getDistance(),
                new Stations(upStation, downStation));
    }

    public StationLineResponse of(StationLine savedStationLine) {

        Stations stations = savedStationLine.getStations();
        Station upStations = stations.getUpStations();
        Station downStation = stations.getDownStation();
        List<StationResponse> stationResponses = List.of(
                new StationResponse(upStations.getId(), upStations.getName()),
                new StationResponse(downStation.getId(), downStation.getName()));
        return new StationLineResponse(
                savedStationLine.getId(), savedStationLine.getName(), savedStationLine.getColor(), stationResponses
        );
    }
}