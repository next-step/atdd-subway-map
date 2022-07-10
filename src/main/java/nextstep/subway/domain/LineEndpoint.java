package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationResponse;

public class LineEndpoint {
    private final List<Station> stations;

    public LineEndpoint(Station upStation,Station downStation) {
        this.stations = new ArrayList<>();
        stations.add(upStation);
        stations.add(downStation);
    }

    public List<StationResponse> toStationResponse() {
        return stations.stream()
            .map(v->new StationResponse(v.getId(),v.getName()))
            .collect(Collectors.toList());
    }
}
