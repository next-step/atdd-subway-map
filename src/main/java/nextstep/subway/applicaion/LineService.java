package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        List<Station> findStations = findUpAndDownStation(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Line createLine = lineRepository.save(lineRequest.toLine());

        return createLineResponse(createLine, findStations);
    }

    public List<LineResponse> findAllLines() {
        List<Line> findLines = lineRepository.findAll();
        return findLines.stream()
                .map(line -> createLineResponse(line, findUpAndDownStation(line.getUpStationId(), line.getDownStationId())))
                .collect(toList());
    }

    private List<Station> findUpAndDownStation(Long upStationId, Long downStationId) {
        return stationRepository.findAllById(Arrays.asList(upStationId, downStationId));
    }

    private LineResponse createLineResponse(Line line, List<Station> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations.stream()
                        .map(this::createStationResponse)
                        .collect(toList())
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    public LineResponse findLineById(Long id) {
        Line findLine = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));

        List<Station> findStations = findUpAndDownStation(findLine.getUpStationId(), findLine.getDownStationId());

        return createLineResponse(findLine, findStations);
    }
}
