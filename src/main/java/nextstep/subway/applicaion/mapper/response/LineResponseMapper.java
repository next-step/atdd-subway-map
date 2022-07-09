package nextstep.subway.applicaion.mapper.response;

import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineResponseMapper implements ResponseMapper<Line, LineResponse> {

    private final StationRepository stationRepository;
    private final StationResponseMapper stationResponseMapper;

    public LineResponseMapper(StationRepository stationRepository, StationResponseMapper stationResponseMapper) {
        this.stationRepository = stationRepository;
        this.stationResponseMapper = stationResponseMapper;
    }

    @Override
    public LineResponse map(Line line) {
        List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream().map(stationResponseMapper::map).collect(Collectors.toList())
        );
    }
}
