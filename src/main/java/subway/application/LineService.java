package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.util.Finder;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.dto.LineCreateRequest;
import subway.dto.LineEditRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final Finder finder;

    public LineService(final LineRepository lineRepository, final StationService stationService, final Finder finder) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.finder = finder;
    }

    private Line findLineBy(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }

    public LineResponse getBy(final Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);

        return LineResponse.from(line, StationResponse.by(line.getStations()));
    }

    public List<LineResponse> getList() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.from(line, StationResponse.by(line.getStations())))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Long save(final LineCreateRequest lineCreateRequest) {
        Line line = convertToLineBy(lineCreateRequest);
        lineRepository.save(line);
        return line.getId();
    }

    private Line convertToLineBy(final LineCreateRequest lineCreateRequest) {
        List<Station> stations = stationService
                .findAllById(List.of(lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId()));

        return new Line(
                lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                finder.findStationById(stations, lineCreateRequest.getUpStationId()),
                finder.findStationById(stations, lineCreateRequest.getDownStationId()),
                lineCreateRequest.getDistance()
        );
    }

    @Transactional
    public void edit(final Long lineId, final LineEditRequest lineEditRequest) {
        Line line = findLineBy(lineId);

        line.modify(lineEditRequest.getName(), lineEditRequest.getColor(), lineEditRequest.getDistance());
    }

    @Transactional
    public void delete(final Long lineId) {
        Line line = findLineBy(lineId);
        lineRepository.delete(line);
    }
}
