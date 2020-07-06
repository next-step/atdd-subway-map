package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationCreateRequest;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private static Logger log = LoggerFactory.getLogger(LineService.class);
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Line saveLine(LineRequest request) {
        return lineRepository.save(request.toLine());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> LineResponse.of(line, mapToLineStationResponse(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id)
                .map(it -> LineResponse.of(it, mapToLineStationResponse(it)))
                .orElseThrow(RuntimeException::new);
    }

    private List<LineStationResponse> mapToLineStationResponse(Line it) {
        return it.getLineStations().stream()
                .map(lineStation -> LineStationResponse.of(findStationById(lineStation.getStationId()), lineStation))
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void createLineStation(Long lineId, LineStationCreateRequest createRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundException::new);

        line.addStation(createRequest.getPreStationId(), createRequest.getStationId(), createRequest.getDistance(), createRequest.getDuration());
    }


    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
