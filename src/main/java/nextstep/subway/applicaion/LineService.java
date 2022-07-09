package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        List<Station> findStations = stationRepository.findAllById(Arrays.asList(lineRequest.getUpStationId(), lineRequest.getDownStationId()));
        Line createLine = lineRepository.save(lineRequest.toLine(findStations));

        return createLineResponse(createLine);
    }

    private LineResponse createLineResponse(Line createLine) {
        return new LineResponse(
                createLine.getId(),
                createLine.getName(),
                createLine.getColor(),
                createLine.getStations().stream()
                        .map(this::createStationResponse)
                        .collect(Collectors.toList())
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
