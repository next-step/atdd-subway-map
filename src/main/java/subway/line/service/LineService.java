package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.station.repository.Station;
import subway.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(toLineEntity(lineRequest));

        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NullPointerException::new);
    }

    public LineResponse getLine(Long id) {
        return LineResponse.of(findLine(id));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = findLine(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    private Line toLineEntity(LineRequest lineRequest) {
        Station upStation = stationService.findStation(lineRequest.getUpStationId());
        Station downStation = stationService.findStation(lineRequest.getDownStationId());

        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                upStation,
                downStation,
                lineRequest.getDistance());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
